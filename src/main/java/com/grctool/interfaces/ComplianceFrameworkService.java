package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkUpdateDTO;

public interface ComplianceFrameworkService {
    ComplianceFrameworkResponseDTO createFramework(ComplianceFrameworkCreateDTO dto);

    List<ComplianceFrameworkResponseDTO> getAllFrameworks();

    ComplianceFrameworkResponseDTO getFrameworkById(UUID id);

    ComplianceFrameworkResponseDTO updateFramework(UUID id, ComplianceFrameworkUpdateDTO dto);

    
}
