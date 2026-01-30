package com.grctool.dto.compliance;

import java.util.UUID;

import com.grctool.enums.ComplianceStatus;

public record ComplianceControlResponseDTO(
        UUID id,
        String name,
        String controlCode,
        String description,
        ComplianceStatus status,
        UUID frameworkId
) {}
