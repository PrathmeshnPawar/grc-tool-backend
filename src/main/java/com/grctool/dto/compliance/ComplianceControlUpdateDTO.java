package com.grctool.dto.compliance;

import com.grctool.enums.FrameworkStatus;

public record ComplianceControlUpdateDTO(
        String name,
        String description,
        FrameworkStatus status
) {}
