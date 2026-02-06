package com.grctool.mapper;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkUpdateDTO;
import com.grctool.model.ComplianceControl;
import com.grctool.model.ComplianceFramework;

@Component
public class ComplianceMapper {

    // INBOUND: Create DTO -> Entity
    public ComplianceControl toEntity(ComplianceControlCreateDTO dto) {
        if (dto == null)
            return null;

        ComplianceControl control = new ComplianceControl();
        control.setName(dto.name()); // Record syntax: name() not getName()
        control.setControlCode(dto.controlCode());
        control.setDescription(dto.description());
        // Status is usually set to PENDING by default in the Service
        return control;
    }

    // OUTBOUND: Entity -> Response DTO
    public ComplianceControlResponseDTO toControlResponse(ComplianceControl control) {
        if (control == null)
            return null;
        return new ComplianceControlResponseDTO(
                control.getId(), control.getName(), control.getControlCode(),
                control.getDescription(), control.getStatus(),
                control.getFramework() != null ? control.getFramework().getId() : null);
    }

    // List helper
    public List<ComplianceControlResponseDTO> toControlResponseList(
            List<ComplianceControl> controls) {
        return controls.stream().map(this::toControlResponse).toList();
    }

    // REFINED: Clear name and correct logic
    public ComplianceFramework toEntity(ComplianceFrameworkCreateDTO dto) {
        if (dto == null)
            return null;

        // 1. Correct instantiation of the Entity Class
        ComplianceFramework framework = new ComplianceFramework();

        // 2. Correct mapping from the immutable Record
        framework.setName(dto.name());
        framework.setVersion(dto.version());
        framework.setDescription(dto.description());

        return framework;
    }

    public void updateEntityFromDto(ComplianceFrameworkUpdateDTO dto, ComplianceFramework framework) {
        if (dto == null || framework == null)
            return;

        dto.name().ifPresent(framework::setName);
        dto.version().ifPresent(framework::setVersion);
        dto.description().ifPresent(framework::setDescription);

    }

    public ComplianceFrameworkResponseDTO toFrameworkResponse(ComplianceFramework f) {
        if (f == null)
            return null;
        return new ComplianceFrameworkResponseDTO(
                f.getId(), f.getName(), f.getVersion(), f.getDescription());
    }

    // LIST HELPER
    public List<ComplianceFrameworkResponseDTO> toFrameworkResponseList(List<ComplianceFramework> frameworks) {
        if (frameworks == null)
            return Collections.emptyList();
        return frameworks.stream()
                .map(this::toFrameworkResponse)
                .toList();
    }
}