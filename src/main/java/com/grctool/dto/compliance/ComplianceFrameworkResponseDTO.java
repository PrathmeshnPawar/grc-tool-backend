package com.grctool.dto.compliance;

import java.util.UUID;

public record ComplianceFrameworkResponseDTO(
        UUID id,
        String name,
        String version,
        String description,
        String status,           // New
        String category,         // New
        String regulatoryBody,   // New
        String referenceLink,    // New
        UUID ownerId,            // New
        String ownerName         // New
) {}