package com.grctool.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.audit.AuditCreateDTO;
import com.grctool.dto.audit.AuditResponseDTO;
import com.grctool.dto.audit.AuditUpdateDTO;
import com.grctool.enums.AuditStatus;
import com.grctool.interfaces.AuditService;
import com.grctool.model.Audit;
import com.grctool.model.ComplianceControl;
import com.grctool.model.Policy;
import com.grctool.model.Risk;
import com.grctool.model.User;
import com.grctool.repository.AuditRepository;
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

    // ---------------- CREATE ----------------

    @Override
    public AuditResponseDTO createAudit(AuditCreateDTO dto) {

        Audit audit = new Audit();
        audit.setName(dto.name());
        audit.setStartDate(dto.startDate());
        audit.setEndDate(dto.endDate());
        audit.setStatus(AuditStatus.PLANNED);

        Risk risk = riskRepository.findById(dto.riskId())
                .orElseThrow(() -> new EntityNotFoundException("Risk not found"));
        audit.setRisk(risk);

        User auditor = userRepository.findById(dto.leadAuditorId())
                .orElseThrow(() -> new EntityNotFoundException("Auditor not found"));
        audit.setLeadAuditor(auditor);

        if (dto.testedControlIds() != null) {
            audit.setTestedControls(
                    controlRepository.findAllById(dto.testedControlIds()).stream().collect(Collectors.toSet())
            );
        }

        if (dto.reviewedPolicyIds() != null) {
            audit.setReviewedPolicies(
                    policyRepository.findAllById(dto.reviewedPolicyIds()).stream().collect(Collectors.toSet())
            );
        }

        return toResponse(auditRepository.save(audit));
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

    // ---------------- STATE ----------------

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

    // ---------------- READ ----------------

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAuditsByStatus(AuditStatus status) {
        return auditRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponseDTO> getAuditsByRisk(UUID riskId) {
        return auditRepository.findByRisk_Id(riskId)
                .stream()
                .map(this::toResponse)
                .toList();
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
                        : Set.of()
        );
    }
}
