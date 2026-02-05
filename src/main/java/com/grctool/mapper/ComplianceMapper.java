package com.grctool.mapper;

import org.springframework.stereotype.Component;

import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.model.ComplianceControl;
import com.grctool.model.ComplianceFramework;

@Component
public class ComplianceMapper {
    public ComplianceControlResponseDTO toControlResponse(ComplianceControl control) {
        if (control == null) return null;
        return new ComplianceControlResponseDTO(
            control.getId(), control.getName(), control.getControlCode(),
            control.getDescription(), control.getStatus(),
            control.getFramework() != null ? control.getFramework().getId() : null
        );
    }

    public ComplianceFrameworkResponseDTO toFrameworkResponse(ComplianceFramework f) {
        if (f == null) return null;
        return new ComplianceFrameworkResponseDTO(
            f.getId(), f.getName(), f.getVersion(), f.getDescription()
        );
    }
}