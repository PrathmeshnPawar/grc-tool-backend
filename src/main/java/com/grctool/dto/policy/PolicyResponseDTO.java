package com.grctool.dto.policy;

import java.util.Set;
import java.util.UUID;

import com.grctool.enums.PolicyStatus;

public record PolicyResponseDTO(
        UUID id,
        String title,
        String version,
        String description,
        String content,
        PolicyStatus status,
        UUID ownerId,
        UUID frameworkId,
        Set<UUID> controlIds
) {}
