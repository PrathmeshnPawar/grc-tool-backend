package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.enums.ComplianceStatus;

public interface ComplianceService {

    ComplianceControlResponseDTO createControl(ComplianceControlCreateDTO dto);

    void changeControlStatus(UUID controlId, ComplianceStatus status);

    List<ComplianceControlResponseDTO> getControlsByFramework(UUID frameworkId);
}
