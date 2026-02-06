package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.enums.ComplianceStatus;
import com.grctool.interfaces.ComplianceControlService;
import com.grctool.mapper.ComplianceMapper;
import com.grctool.model.ComplianceControl;
import com.grctool.model.ComplianceFramework;
import com.grctool.repository.ComplianceControlRepository;
import com.grctool.repository.ComplianceFrameworkRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplianceControlServiceImpl implements ComplianceControlService {

    private final ComplianceControlRepository controlRepository;
    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceMapper mapper;

    @Override
    @Transactional // Always for writes
    public ComplianceControlResponseDTO createControl(ComplianceControlCreateDTO dto) {
        // 1. Validate Framework exists
        ComplianceFramework framework = frameworkRepository.findById(dto.frameworkId())
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));

        // 2. Map DTO to Entity
        ComplianceControl control = mapper.toEntity(dto);
        
        // 3. Set business logic fields
        control.setFramework(framework);
        control.setStatus(ComplianceStatus.PENDING);

        // 4. Save and Map back to Response
        return mapper.toControlResponse(controlRepository.save(control));
    }

    @Override
    @Transactional // Required to persist the status change
    public void changeControlStatus(UUID controlId, ComplianceStatus status) {
        ComplianceControl control = controlRepository.findById(controlId)
                .orElseThrow(() -> new EntityNotFoundException("Control not found"));

        control.setStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceControlResponseDTO> getControlsByFramework(UUID frameworkId) {
        List<ComplianceControl> controls = controlRepository.findByFramework_Id(frameworkId);
        return mapper.toControlResponseList(controls);
    }
}