package com.grctool.dto.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grctool.enums.AuditStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record AuditResponseDTO(
    UUID id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime createdAt, // 5th parameter - Matches your mapper's logic
    AuditStatus status, // 6th
    UUID riskId, // 7th
    UUID leadAuditorId, // 8th
    Set<UUID> testedControlIds, // 9th
    Set<UUID> reviewedPolicyIds, // 10th
    String globalEvidenceSummary // 11th
) {}
