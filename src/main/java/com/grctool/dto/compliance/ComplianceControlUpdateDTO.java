package com.grctool.dto.compliance;

import com.grctool.enums.ComplianceStatus;

public record ComplianceControlUpdateDTO(
        String name,
        String description,
        ComplianceStatus status
) {}
