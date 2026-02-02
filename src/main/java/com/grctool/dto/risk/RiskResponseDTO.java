package com.grctool.dto.risk;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grctool.enums.RiskCategory;
import com.grctool.enums.RiskStatus;

public record RiskResponseDTO(
        UUID id,
        String title,
        String description,
        RiskCategory category,
        int impact,
        int likelihood,
        int riskScore,
        RiskStatus status,
       LocalDateTime createdAt,
        UUID ownerId,
        UUID incidentId
) {}
