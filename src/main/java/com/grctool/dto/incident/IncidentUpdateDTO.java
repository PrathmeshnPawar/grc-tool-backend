package com.grctool.dto.incident;

import java.time.LocalDate;

import com.grctool.enums.IncidentSeverity;
import com.grctool.model.User;

public record IncidentUpdateDTO(
        User reportedBy,
        String title,
        String description,
        IncidentSeverity severity,
        LocalDate dateReported
) {}
