package com.grctool.dto.audit;

import java.util.UUID;

import com.grctool.enums.AuditResultStatus;

public record AuditResultRequestDTO(
    UUID controlId,
    AuditResultStatus status,
    String findings,
    String evidenceUrl // NEW: Link to the uploaded document or proof
) {}