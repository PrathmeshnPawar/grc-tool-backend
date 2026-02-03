package com.grctool.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.audit.AuditCreateDTO;
import com.grctool.dto.audit.AuditResponseDTO;
import com.grctool.dto.audit.AuditResultRequestDTO;
import com.grctool.dto.audit.AuditUpdateDTO;
import com.grctool.enums.AuditResultStatus;
import com.grctool.enums.AuditStatus;
import com.grctool.interfaces.AuditService;
import com.grctool.model.Audit;
import com.grctool.model.AuditResult;
import com.grctool.model.ComplianceControl;
import com.grctool.model.Policy;
import com.grctool.model.Risk;
import com.grctool.model.User;
import com.grctool.repository.AuditRepository;
import com.grctool.repository.AuditResultRepository;
import com.grctool.repository.ComplianceControlRepository;
import com.grctool.repository.PolicyRepository;
import com.grctool.repository.RiskRepository;
import com.grctool.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final RiskRepository riskRepository;
    private final UserRepository userRepository;
    private final ComplianceControlRepository controlRepository;
    private final PolicyRepository policyRepository;
    private final AuditResultRepository auditResultRepository;

    // ---------------- CREATE ----------------

    @Override
    public AuditResponseDTO createAudit(AuditCreateDTO dto) {
        Audit audit = new Audit();
        audit.setName(dto.name());
        audit.setStartDate(dto.startDate());
        audit.setEndDate(dto.endDate());
        audit.setStatus(AuditStatus.PLANNED);

        // Link Lead Auditor
        User auditor = userRepository.findById(dto.leadAuditorId())
                .orElseThrow(() -> new EntityNotFoundException("Auditor not found"));
        audit.setLeadAuditor(auditor);

        // Link Risk
        Risk risk = riskRepository.findById(dto.riskId())
                .orElseThrow(() -> new EntityNotFoundException("Risk not found"));
        audit.setRisk(risk);

        // Link Many-to-Many Tested Controls
        if (dto.testedControlIds() != null && !dto.testedControlIds().isEmpty()) {
            audit.setTestedControls(new HashSet<>(controlRepository.findAllById(dto.testedControlIds())));
        }

        // Link Many-to-Many Reviewed Policies
        if (dto.reviewedPolicyIds() != null && !dto.reviewedPolicyIds().isEmpty()) {
            audit.setReviewedPolicies(new HashSet<>(policyRepository.findAllById(dto.reviewedPolicyIds())));
        }

        // Save the audit first to get an ID for AuditResults
        Audit savedAudit = auditRepository.save(audit);

        // Initialize empty results for each tested control
        if (savedAudit.getTestedControls() != null) {
            List<AuditResult> initialResults = savedAudit.getTestedControls().stream()
                    .map(control -> {
                        AuditResult result = new AuditResult();
                        result.setAudit(savedAudit);
                        result.setControl(control);
                        result.setStatus(AuditResultStatus.INCONCLUSIVE);
                        return result;
                    }).toList();
            auditResultRepository.saveAll(initialResults);
        }

        return toResponse(savedAudit);
    }

    // ---------------- READ ----------------

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAllAudits() {
        return auditRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditResponseDTO getAuditById(UUID auditId) {
        return toResponse(getAudit(auditId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAuditsByStatus(AuditStatus status) {
        return auditRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAuditsByRisk(UUID riskId) {
        return auditRepository.findByRisk_Id(riskId).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------- UPDATE ----------------

    @Override
    public AuditResponseDTO updateAudit(UUID auditId, AuditUpdateDTO dto) {
        Audit audit = getAudit(auditId);
        audit.setName(dto.name());
        audit.setStartDate(dto.startDate());
        audit.setEndDate(dto.endDate());
        return toResponse(audit);
    }

    @Override
    public void assignLeadAuditor(UUID auditId, UUID auditorId) {
        Audit audit = getAudit(auditId);
        User auditor = userRepository.findById(auditorId)
                .orElseThrow(() -> new EntityNotFoundException("Auditor not found"));
        audit.setLeadAuditor(auditor);
    }

    @Override
    public void changeStatus(UUID auditId, AuditStatus status) {
        Audit audit = getAudit(auditId);
        audit.setStatus(status);
    }

    // ---------------- RESULT SUBMISSION ----------------

    @Override
    public void submitControlResult(UUID auditId, AuditResultRequestDTO dto) {
        // Find the specific result record initialized during createAudit
        AuditResult result = auditResultRepository.findByAuditIdAndControlId(auditId, dto.controlId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No result record found for this control in this audit"));

        result.setStatus(dto.status());
        result.setFindings(dto.findings());
        result.setEvidencePath(dto.evidencePath()); // Now correctly persisting the evidence link

        auditResultRepository.save(result);
    }

    // ---------------- HELPERS ----------------

    private Audit getAudit(UUID id) {
        return auditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Audit not found"));
    }

    private AuditResponseDTO toResponse(Audit audit) {
        return new AuditResponseDTO(
                audit.getId(),
                audit.getName(),
                audit.getStartDate(),
                audit.getEndDate(),
                audit.getStatus(),
                audit.getRisk() != null ? audit.getRisk().getId() : null,
                audit.getLeadAuditor() != null ? audit.getLeadAuditor().getId() : null,
                audit.getTestedControls() != null
                        ? audit.getTestedControls().stream().map(ComplianceControl::getId).collect(Collectors.toSet())
                        : Set.of(),
                audit.getReviewedPolicies() != null
                        ? audit.getReviewedPolicies().stream().map(Policy::getId).collect(Collectors.toSet())
                        : Set.of(),
                null // Matches the 10th parameter (globalEvidenceSummary) in your AuditResponseDTO
        );
    }

    //---------------- COMPLETE AUDIT ----------------
    @Override
    public void completeAudit(UUID auditId) {
        Audit audit = auditRepository.findById(auditId).orElseThrow();
    
    // 1. Update tested controls status
    audit.getTestedControls().forEach(control -> {
        // Logic to update ComplianceStatus based on evidence
    });
    
    // 2. Check if the linked Risk likelihood needs adjustment
    if (audit.getRisk() != null) {
        // Logic to re-calculate riskScore if controls failed
    }
    
    audit.setStatus(AuditStatus.COMPLETED);
    auditRepository.save(audit);
}
}