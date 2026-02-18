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

    // --- CONTROLS ---

    public ComplianceControl toEntity(ComplianceControlCreateDTO dto) {
        if (dto == null) return null;

        ComplianceControl control = new ComplianceControl();
        control.setName(dto.name());
        control.setControlCode(dto.controlCode());
        control.setDescription(dto.description());
        return control;
    }

    public ComplianceControlResponseDTO toControlResponse(ComplianceControl control) {
        if (control == null) return null;
        return new ComplianceControlResponseDTO(
                control.getId(), 
                control.getName(), 
                control.getControlCode(),
                control.getDescription(), 
                control.getStatus(),
                control.getFramework() != null ? control.getFramework().getId() : null);
    }

    public List<ComplianceControlResponseDTO> toControlResponseList(List<ComplianceControl> controls) {
        if (controls == null) return Collections.emptyList();
        return controls.stream().map(this::toControlResponse).toList();
    }

    // --- FRAMEWORKS ---

    public ComplianceFramework toEntity(ComplianceFrameworkCreateDTO dto) {
        if (dto == null) return null;

        ComplianceFramework framework = new ComplianceFramework();
        framework.setName(dto.name());
        framework.setVersion(dto.version());
        framework.setDescription(dto.description());
        
        // Professional Metadata Mapping
        framework.setCategory(dto.category());
        framework.setRegulatoryBody(dto.regulatoryBody());
        framework.setReferenceLink(dto.referenceLink());

        return framework;
    }

    public void updateEntityFromDto(ComplianceFrameworkUpdateDTO dto, ComplianceFramework framework) {
        if (dto == null || framework == null) return;

        dto.name().ifPresent(framework::setName);
        dto.version().ifPresent(framework::setVersion);
        dto.description().ifPresent(framework::setDescription);
        
        // Handling optional professional updates
        dto.category().ifPresent(framework::setCategory);
        dto.regulatoryBody().ifPresent(framework::setRegulatoryBody);
        dto.referenceLink().ifPresent(framework::setReferenceLink);
    }

    public ComplianceFrameworkResponseDTO toFrameworkResponse(ComplianceFramework f) {
        if (f == null) return null;
        
        return new ComplianceFrameworkResponseDTO(
                f.getId(), 
                f.getName(), 
                f.getVersion(), 
                f.getDescription(),
                f.getStatus() != null ? f.getStatus().name() : "DRAFT", // Lifecycle Status
                f.getCategory(),
                f.getRegulatoryBody(),
                f.getReferenceLink(),
                f.getOwner() != null ? f.getOwner().getId() : null,
                f.getOwner() != null ? f.getOwner().getName() : "Unassigned" // UX Benefit: Provide name
        );
    }

    public List<ComplianceFrameworkResponseDTO> toFrameworkResponseList(List<ComplianceFramework> frameworks) {
        if (frameworks == null) return Collections.emptyList();
        return frameworks.stream().map(this::toFrameworkResponse).toList();
    }
}