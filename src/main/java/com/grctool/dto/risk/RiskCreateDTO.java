package com.grctool.dto.risk;

import java.util.UUID;

import com.grctool.enums.RiskCategory;

public record RiskCreateDTO(
        String title,
        String description,
        RiskCategory category,
        int impact,
        int likelihood,
        UUID ownerId,
        UUID incidentId
) {}
