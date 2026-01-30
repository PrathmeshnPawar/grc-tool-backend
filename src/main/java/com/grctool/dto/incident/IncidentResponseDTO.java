package com.grctool.dto.incident;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.grctool.enums.IncidentSeverity;
import com.grctool.enums.IncidentStatus;

public record IncidentResponseDTO(
        UUID id,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentStatus status,
        LocalDate dateReported,
        UUID reportedById,
        Set<UUID> riskIds
) {}
