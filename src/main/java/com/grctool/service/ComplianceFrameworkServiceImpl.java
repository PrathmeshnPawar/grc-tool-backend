package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.interfaces.ComplianceFrameworkService;
import com.grctool.model.ComplianceFramework;
import com.grctool.repository.ComplianceFrameworkRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplianceFrameworkServiceImpl implements ComplianceFrameworkService {

    private final ComplianceFrameworkRepository frameworkRepository;

    @Override
    public ComplianceFrameworkResponseDTO createFramework(ComplianceFrameworkCreateDTO dto) {
        ComplianceFramework framework = new ComplianceFramework();
        framework.setName(dto.name());
        framework.setVersion(dto.version()); // Ensure version is in your model
        framework.setDescription(dto.description());
        return toResponse(frameworkRepository.save(framework));
    }

    // ADD THIS METHOD TO RESOLVE THE ERROR
    @Override
    public ComplianceFrameworkResponseDTO updateFramework(UUID id, ComplianceFrameworkCreateDTO dto) {
        ComplianceFramework framework = frameworkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));

        framework.setName(dto.name());
        framework.setVersion(dto.version());
        framework.setDescription(dto.description());

        return toResponse(frameworkRepository.save(framework));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceFrameworkResponseDTO> getAllFrameworks() {
        return frameworkRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComplianceFrameworkResponseDTO getFrameworkById(UUID id) {
        return frameworkRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));
    }

    private ComplianceFrameworkResponseDTO toResponse(ComplianceFramework f) {
        return new ComplianceFrameworkResponseDTO(
                f.getId(), 
                f.getName(), 
                f.getVersion(), // Use the actual version from the model
                f.getDescription()
        );
    }
}