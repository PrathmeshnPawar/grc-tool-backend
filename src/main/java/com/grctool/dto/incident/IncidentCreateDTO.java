package com.grctool.dto.incident;

import java.time.LocalDate;
import java.util.UUID;

import com.grctool.enums.IncidentSeverity;

public record IncidentCreateDTO(
        String title,
        String description,
        IncidentSeverity severity,
        LocalDate dateReported,
        String reportedBy,
        UUID reportedById
) {}
