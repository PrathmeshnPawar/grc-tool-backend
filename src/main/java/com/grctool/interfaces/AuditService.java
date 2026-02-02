package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.audit.AuditCreateDTO;
import com.grctool.dto.audit.AuditResponseDTO;
import com.grctool.dto.audit.AuditResultRequestDTO;
import com.grctool.dto.audit.AuditUpdateDTO;
import com.grctool.enums.AuditStatus;

public interface AuditService {

    AuditResponseDTO createAudit(AuditCreateDTO dto);

    AuditResponseDTO updateAudit(UUID auditId, AuditUpdateDTO dto);

    void assignLeadAuditor(UUID auditId, UUID auditorId);

    void changeStatus(UUID auditId, AuditStatus status);

    List<AuditResponseDTO> getAuditsByStatus(AuditStatus status);

    List<AuditResponseDTO> getAuditsByRisk(UUID riskId);

    void submitControlResult(UUID auditId, AuditResultRequestDTO dto);

    AuditResponseDTO getAuditById(UUID auditId);

    List<AuditResponseDTO> getAllAudits();
}
