package com.grctool.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record AuditCreateDTO(
    String name,
    @JsonProperty("start_date") LocalDate startDate, // Maps from snake_case
    @JsonProperty("end_date") LocalDate endDate,
    // Maps from snake_case

    UUID riskId,
    UUID leadAuditorId,
    Set<UUID> testedControlIds,
    Set<UUID> reviewedPolicyIds
) {}
