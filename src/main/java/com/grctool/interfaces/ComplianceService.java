package com.grctool.interfaces;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface ComplianceService {

    ComplianceFrameworkResponseDTO createFramework(ComplianceFrameworkCreateDTO dto);

    ComplianceFrameworkResponseDTO updateFramework(UUID id, ComplianceFrameworkUpdateDTO dto);

    List<ComplianceFrameworkResponseDTO> getAllFrameworks();

    

    
}