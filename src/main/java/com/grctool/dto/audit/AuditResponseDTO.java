package com.grctool.dto.audit;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import com.grctool.enums.AuditStatus;

public record AuditResponseDTO(
    UUID id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    AuditStatus status,
    UUID riskId,
    UUID leadAuditorId,
    Set<UUID> testedControlIds,
    Set<UUID> reviewedPolicyIds,
    String globalEvidenceSummary // NEW: Optional summary link for the whole audit
) {}