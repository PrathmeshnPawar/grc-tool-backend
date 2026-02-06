package com.grctool.service.auditService;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.audit.*;
import com.grctool.enums.*;
import com.grctool.interfaces.AuditService;
import com.grctool.model.*;
import com.grctool.repository.*;
import com.grctool.mapper.AuditMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Best practice: default to read-only
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final RiskRepository riskRepository;
    private final UserRepository userRepository;
    private final ComplianceControlRepository controlRepository;
    private final PolicyRepository policyRepository;
    private final AuditResultRepository auditResultRepository;
    private final AuditMapper auditMapper;

    // ---------------- CREATE ----------------

    @Override
    @Transactional // Override read-only for writes
    public AuditResponseDTO createAudit(AuditCreateDTO dto) {
        // 1. Fetch dependencies (The Service's job to provide these to the Mapper)
        User auditor = userRepository.findById(dto.leadAuditorId())
                .orElseThrow(() -> new EntityNotFoundException("Auditor not found"));

        Risk risk = riskRepository.findById(dto.riskId())
                .orElseThrow(() -> new EntityNotFoundException("Risk not found"));

        Set<ComplianceControl> controls = new HashSet<>(controlRepository.findAllById(dto.testedControlIds()));
        Set<Policy> policies = new HashSet<>(policyRepository.findAllById(dto.reviewedPolicyIds()));

        // 2. Map to Transient Entity
        Audit audit = auditMapper.toEntity(dto, auditor, risk, controls, policies);

        // 3. Persist Audit (Generates the ID)
        Audit savedAudit = auditRepository.save(audit);

        // 4. Lifecycle Orchestration: Generate AuditResult placeholders
        if (!controls.isEmpty()) {
            List<AuditResult> initialResults = controls.stream()
                    .map(control -> {
                        AuditResult result = new AuditResult();
                        result.setAudit(savedAudit);
                        result.setControl(control);
                        result.setStatus(AuditResultStatus.INCONCLUSIVE);
                        return result;
                    }).toList();
            auditResultRepository.saveAll(initialResults);
        }

        return auditMapper.toResponse(savedAudit);
    }

    // ---------------- READ ----------------

    @Override
    public List<AuditResponseDTO> getAllAudits() {
        return auditMapper.toResponseList(auditRepository.findAll());
    }

    @Override
    public AuditResponseDTO getAuditById(UUID auditId) {
        return auditMapper.toResponse(getAudit(auditId));
    }

    @Override
    public List<AuditResponseDTO> getAuditsByStatus(AuditStatus status) {
        return auditMapper.toResponseList(auditRepository.findByStatus(status));
    }

    // ---------------- UPDATE ----------------

    @Override
    @Transactional
    public AuditResponseDTO updateAudit(UUID auditId, AuditUpdateDTO dto) {
        Audit audit = getAudit(auditId);

        // Use the mapper to update fields
        auditMapper.updateEntity(audit, dto);

        // Dirty checking automatically saves this at method end
        return auditMapper.toResponse(audit);
    }

    @Override
    @Transactional
    public void changeStatus(UUID auditId, AuditStatus status) {
        Audit audit = getAudit(auditId);
        audit.setStatus(status);
    }

    // ---------------- RESULT SUBMISSION ----------------

    // ---------------- COMPLETE AUDIT ----------------

    @Override
    @Transactional
    public void completeAudit(UUID auditId) {
        Audit audit = getAudit(auditId);

        // 1. Validation Logic
        List<AuditResult> results = auditResultRepository.findByAuditId(auditId);
        boolean allFinished = results.stream()
                .noneMatch(r -> r.getStatus() == AuditResultStatus.INCONCLUSIVE);

        if (!allFinished) {
            throw new IllegalStateException("Cannot complete audit while results are inconclusive");
        }

        // 2. The Verdict
        boolean failuresFound = results.stream()
                .anyMatch(r -> r.getStatus() == AuditResultStatus.FAIL);

        // 3. The Ripple Effect (Using failuresFound)
        if (failuresFound) {
            // Option A: Update the Risk Likelihood
            if (audit.getRisk() != null) {
                // Logic: If controls fail, the risk is more likely to happen
                int currentLikelihood = audit.getRisk().getLikelihood();
                audit.getRisk().setLikelihood(Math.min(currentLikelihood + 1, 5)); // Max cap at 5
            }

            // Option B: Mark the Audit as FAILED/COMPLETED_WITH_ISSUES
            // audit.setStatus(AuditStatus.COMPLETED_WITH_FAILURES);
        }

        // 4. Finalize
        audit.setStatus(AuditStatus.COMPLETED);

        // WIZARD TIP: In a real enterprise app, you'd trigger an Email Service here:
        // emailService.sendAuditSummary(audit.getLeadAuditor(), audit);
    }

    @Override
    public List<AuditResponseDTO> getAuditsByRisk(UUID riskId) {
        // WIZARD TIP: This creates a direct bridge between your Risk and Audit domains
        List<Audit> audits = auditRepository.findByRisk_Id(riskId);
        return auditMapper.toResponseList(audits);
    }

    @Override
    @Transactional
    public void assignLeadAuditor(UUID auditId, UUID auditorId) {
        Audit audit = getAudit(auditId);
        User auditor = userRepository.findById(auditorId)
                .orElseThrow(() -> new EntityNotFoundException("Auditor not found"));

        audit.setLeadAuditor(auditor);
        // Wizard Rule: In a real app, you'd send an email notification to the auditor
        // here.
    }

    @Override
    @Transactional
    public void submitControlResult(UUID auditId, AuditResultRequestDTO dto) {
        // This coordinates the AuditResult sub-resource
        AuditResult result = auditResultRepository.findByAuditIdAndControlId(auditId, dto.controlId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Result slot not found"));

        result.setStatus(dto.status());
        result.setFindings(dto.findings());
        result.setEvidencePath(dto.evidencePath());
    }

    // ---------------- HELPERS ----------------

    private Audit getAudit(UUID id) {
        return auditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Audit not found with ID: " + id));
    }
}