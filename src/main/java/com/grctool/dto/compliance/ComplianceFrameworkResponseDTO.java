package com.grctool.dto.compliance;

import java.util.UUID;

public record ComplianceFrameworkResponseDTO(
        UUID id,
        String name,
        String version,
        String description
) {}
