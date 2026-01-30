package com.grctool.dto.incident;

import java.time.LocalDate;

import com.grctool.enums.IncidentSeverity;

public record IncidentUpdateDTO(
        String title,
        String description,
        IncidentSeverity severity,
        LocalDate dateReported
) {}
