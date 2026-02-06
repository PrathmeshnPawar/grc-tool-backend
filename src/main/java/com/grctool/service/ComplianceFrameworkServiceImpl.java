package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkUpdateDTO;
import com.grctool.interfaces.ComplianceFrameworkService;
import com.grctool.mapper.ComplianceMapper;
import com.grctool.model.ComplianceFramework;
import com.grctool.repository.ComplianceFrameworkRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplianceFrameworkServiceImpl implements ComplianceFrameworkService {

    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceMapper mapper;

    @Override
    @Transactional
    public ComplianceFrameworkResponseDTO createFramework(ComplianceFrameworkCreateDTO dto) {
        ComplianceFramework framework = frameworkRepository.save(mapper.toEntity(dto));
        return mapper.toFrameworkResponse(framework);
    }

    // ADD THIS METHOD TO RESOLVE THE ERROR
    @Override
    @Transactional
    public ComplianceFrameworkResponseDTO updateFramework(UUID id, ComplianceFrameworkUpdateDTO dto) {
        // 1. Fetch the real, managed entity from DB
        ComplianceFramework framework = frameworkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));

        // 2. Apply the partial updates
        mapper.updateEntityFromDto(dto, framework);

        // 3. Return response (Hibernate auto-detects changes and saves)
        return mapper.toFrameworkResponse(framework);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceFrameworkResponseDTO> getAllFrameworks() {
        List<ComplianceFramework> frameworks = frameworkRepository.findAll();
        return mapper.toFrameworkResponseList(frameworks);
    }

    @Override
    public ComplianceFrameworkResponseDTO getFrameworkById(UUID id) {
        ComplianceFramework framework = frameworkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Framework not found"));
        return mapper.toFrameworkResponse(framework);

    }

}