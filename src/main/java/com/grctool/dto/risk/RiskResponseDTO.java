package com.grctool.dto.risk;

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
        UUID ownerId,
        UUID incidentId
) {}
