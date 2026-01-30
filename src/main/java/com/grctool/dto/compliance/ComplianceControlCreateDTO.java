package com.grctool.dto.compliance;

import java.util.UUID;

import com.grctool.enums.ComplianceStatus;

public record ComplianceControlCreateDTO(
        String name,
        String controlCode,
        String description,
        UUID frameworkId,
        ComplianceStatus status
) {}
