package com.grctool.dto.policy;

import java.util.Set;
import java.util.UUID;

public record PolicyCreateDTO(
        String title,
        String version,
        String description,
        String content,
        UUID ownerId,
        UUID frameworkId,
        Set<UUID> controlIds
) {}
