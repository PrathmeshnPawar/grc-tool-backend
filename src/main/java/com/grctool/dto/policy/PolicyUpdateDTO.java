package com.grctool.dto.policy;

import java.util.Set;
import java.util.UUID;

public record PolicyUpdateDTO(
        String title,
        String version,
        String description,
        String content,
        UUID frameworkId,
        Set<UUID> controlIds
) {}
