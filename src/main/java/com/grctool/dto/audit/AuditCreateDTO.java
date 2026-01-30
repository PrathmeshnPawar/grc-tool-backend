package com.grctool.dto.audit;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record AuditCreateDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        UUID riskId,
        UUID leadAuditorId,
        Set<UUID> testedControlIds,
        Set<UUID> reviewedPolicyIds
) {}
