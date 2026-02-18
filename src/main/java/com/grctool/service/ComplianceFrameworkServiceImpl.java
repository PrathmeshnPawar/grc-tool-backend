package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.dto.compliance.ComplianceFrameworkUpdateDTO;
import com.grctool.enums.FrameworkStatus;
import com.grctool.interfaces.ComplianceFrameworkService;
import com.grctool.mapper.ComplianceMapper;
import com.grctool.model.ComplianceFramework;
import com.grctool.model.User;
import com.grctool.repository.ComplianceFrameworkRepository;
import com.grctool.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplianceFrameworkServiceImpl implements ComplianceFrameworkService {

    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceMapper mapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ComplianceFrameworkResponseDTO createFramework(ComplianceFrameworkCreateDTO dto) {
        // 1. Map basic fields to Entity
        ComplianceFramework framework = mapper.toEntity(dto);

        // 2. Set default Lifecycle state
        framework.setStatus(FrameworkStatus.DRAFT);

        // 3. Resolve and Link the Owner
        if (dto.ownerId() != null) {
            User owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
            framework.setOwner(owner);
        }

        // 4. Persist and return response
        return mapper.toFrameworkResponse(frameworkRepository.save(framework));
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