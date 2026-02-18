package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.enums.FrameworkStatus;

public interface ComplianceControlService {

    ComplianceControlResponseDTO createControl(ComplianceControlCreateDTO dto);

    void changeControlStatus(UUID controlId, FrameworkStatus status);

    List<ComplianceControlResponseDTO> getControlsByFramework(UUID frameworkId);
}
