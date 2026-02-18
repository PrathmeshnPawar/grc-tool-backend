package com.grctool.dto.compliance;

import java.util.UUID;

import com.grctool.enums.FrameworkStatus;

public record ComplianceControlResponseDTO(
        UUID id,
        String name,
        String controlCode,
        String description,
        FrameworkStatus status,
        UUID frameworkId
) {}
