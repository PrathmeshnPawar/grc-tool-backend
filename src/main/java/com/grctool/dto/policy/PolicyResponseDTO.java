package com.grctool.dto.policy;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    Set<UUID> controlIds,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime lastUpdated,
    String filePath,    // Added
    boolean isProcessed // Added
) {}
