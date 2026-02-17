package com.grctool.mapper;

import com.grctool.dto.audit.AuditCreateDTO;
import com.grctool.dto.audit.AuditResponseDTO;
import com.grctool.dto.audit.AuditUpdateDTO;
import com.grctool.enums.AuditStatus;
import com.grctool.model.Audit;
import com.grctool.model.ComplianceControl;
import com.grctool.model.Policy;
import com.grctool.model.Risk;
import com.grctool.model.User;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditMapper {

    public AuditResponseDTO toResponse(Audit audit) {
        return new AuditResponseDTO(
            audit.getId(), // 1. id
            audit.getName(), // 2. name
            audit.getStartDate(), // 3. startDate
            audit.getEndDate(), // 4. endDate
            audit.getCreatedAt(), // 5. createdAt
            audit.getStatus(), // 6. status
            audit.getRisk() != null ? audit.getRisk().getId() : null, // 7. riskId
            audit.getLeadAuditor() != null
                ? audit.getLeadAuditor().getId()
                : null, // 8. leadAuditorId
            audit.getTestedControls() !=
                null // 9. testedControlIds
                ? audit
                      .getTestedControls()
                      .stream()
                      .map(ComplianceControl::getId)
                      .collect(Collectors.toSet())
                : Set.of(),
            audit.getReviewedPolicies() !=
                null // 10. reviewedPolicyIds
                ? audit
                      .getReviewedPolicies()
                      .stream()
                      .map(Policy::getId)
                      .collect(Collectors.toSet())
                : Set.of(),
            null // 11. globalEvidenceSummary
        );
    }

    public Audit toEntity(
        AuditCreateDTO dto,
        User auditor,
        Risk risk,
        Set<ComplianceControl> controls,
        Set<Policy> policies
    ) {
        if (dto == null) return null;

        Audit audit = new Audit();
        audit.setName(dto.name());
        audit.setStartDate(dto.startDate());
        audit.setEndDate(dto.endDate());
        audit.setStatus(AuditStatus.PLANNED);

        // Link Lead Auditor
        audit.setLeadAuditor(auditor);
        audit.setRisk(risk);
        audit.setTestedControls(controls);
        audit.setReviewedPolicies(policies);

        // Save the audit first to get an ID for AuditResults
        return audit;
    }

    public void updateEntity(Audit audit, AuditUpdateDTO dto) {
        if (audit == null || dto == null) return;

        audit.setName(dto.name());
        audit.setStartDate(dto.startDate());
        audit.setEndDate(dto.endDate());
        // audit.setStatus(AuditStatus.PLANNED);
        // Note: Updating relationships (Lead Auditor, Risk, Controls, Policies) should
        // be handled in the Service layer
    }

    public void updateStatus(Audit audit, AuditStatus newStatus) {
        if (audit == null || newStatus == null) return;

        audit.setStatus(newStatus);
    }

    public List<AuditResponseDTO> toResponseList(List<Audit> audits) {
        if (audits == null) return Collections.emptyList();

        return audits
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}
