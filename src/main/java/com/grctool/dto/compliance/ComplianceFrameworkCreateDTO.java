package com.grctool.dto.compliance;

import java.util.UUID;

public record ComplianceFrameworkCreateDTO(
    String name,
    String version,
    String description,
    String category,
    String regulatoryBody,
    String referenceLink,
    UUID ownerId // Links the framework to a specific User
) {}