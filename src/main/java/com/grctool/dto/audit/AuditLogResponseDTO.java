package com.grctool.dto.audit;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponseDTO(
    UUID id,
    String entityName,
    UUID entityId,
    String action,
    String changeDetails,
    String performedBy,
    String ipAddress,
    String userAgent,
    LocalDateTime createdAt
) {}
