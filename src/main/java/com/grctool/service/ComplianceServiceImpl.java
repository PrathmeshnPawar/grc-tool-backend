package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.enums.ComplianceStatus;
import com.grctool.interfaces.ComplianceService;
import com.grctool.model.ComplianceControl;
import com.grctool.model.ComplianceFramework;
import com.grctool.repository.ComplianceControlRepository;
import com.grctool.repository.ComplianceFrameworkRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceControlRepository controlRepository;
    private final ComplianceFrameworkRepository frameworkRepository;

    @Override
    public ComplianceControlResponseDTO createControl(ComplianceControlCreateDTO dto) {

        ComplianceFramework framework = frameworkRepository.findById(dto.frameworkId())
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));

        ComplianceControl control = new ComplianceControl();
        control.setName(dto.name());
        control.setControlCode(dto.controlCode());
        control.setDescription(dto.description());
        control.setStatus(dto.status());
        control.setFramework(framework);

        return toResponse(controlRepository.save(control));
    }

    @Override
    public void changeControlStatus(UUID controlId, ComplianceStatus status) {
        ComplianceControl control = controlRepository.findById(controlId)
                .orElseThrow(() -> new EntityNotFoundException("Control not found"));

        control.setStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceControlResponseDTO> getControlsByFramework(UUID frameworkId) {
        return controlRepository.findByFramework_Id(frameworkId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ComplianceControlResponseDTO toResponse(ComplianceControl control) {
        return new ComplianceControlResponseDTO(
                control.getId(),
                control.getName(),
                control.getControlCode(),
                control.getDescription(),
                control.getStatus(),
                control.getFramework().getId()
        );
    }
}
