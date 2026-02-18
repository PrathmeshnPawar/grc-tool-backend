package com.grctool.dto.compliance;

import java.util.UUID;

import com.grctool.enums.FrameworkStatus;

public record ComplianceControlCreateDTO(
        String name,
        String controlCode,
        String description,
        UUID frameworkId,
        FrameworkStatus status
) {}
