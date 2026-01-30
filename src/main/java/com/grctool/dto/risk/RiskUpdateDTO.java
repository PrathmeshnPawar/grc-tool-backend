package com.grctool.dto.risk;

import com.grctool.enums.RiskCategory;

public record RiskUpdateDTO(
        String title,
        String description,
        RiskCategory category,
        int impact,
        int likelihood
) {}
