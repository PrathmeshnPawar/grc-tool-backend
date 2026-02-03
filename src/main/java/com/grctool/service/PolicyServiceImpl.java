package com.grctool.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;
import com.grctool.interfaces.PolicyService;
import com.grctool.model.ComplianceControl;
import com.grctool.model.Policy;
import com.grctool.repository.ComplianceControlRepository;
import com.grctool.repository.ComplianceFrameworkRepository;
import com.grctool.repository.PolicyRepository;
import com.grctool.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceControlRepository controlRepository;

    @Override
    public PolicyResponseDTO createPolicy(PolicyCreateDTO dto) {

        Policy policy = new Policy();
        policy.setTitle(dto.title());
        policy.setVersion(dto.version());
        policy.setDescription(dto.description());
        policy.setContent(dto.content());
        policy.setStatus(PolicyStatus.DRAFT);

        policy.setOwner(
                userRepository.findById(dto.ownerId())
                        .orElseThrow(() -> new EntityNotFoundException("Owner not found"))
        );

        policy.setFramework(
                frameworkRepository.findById(dto.frameworkId())
                        .orElseThrow(() -> new EntityNotFoundException("Framework not found"))
        );

        if (dto.controlIds() != null) {
            policy.setControls(
                    new HashSet<>(controlRepository.findAllById(dto.controlIds()))
            );
        }

        return toResponse(policyRepository.save(policy));
    }


    @Override 
    public List<PolicyResponseDTO> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public PolicyResponseDTO updatePolicy(UUID policyId, PolicyUpdateDTO dto) {
        Policy policy = getPolicy(policyId);

        policy.setTitle(dto.title());
        policy.setVersion(dto.version());
        policy.setDescription(dto.description());
        policy.setContent(dto.content());

        if (dto.frameworkId() != null) {
            policy.setFramework(
                    frameworkRepository.findById(dto.frameworkId())
                            .orElseThrow(() -> new EntityNotFoundException("Framework not found"))
            );
        }

        if (dto.controlIds() != null) {
            policy.setControls(
                    new HashSet<>(controlRepository.findAllById(dto.controlIds()))
            );
        }

        return toResponse(policy);
    }

    @Override
    public void changeStatus(UUID policyId, PolicyStatus status) {
        getPolicy(policyId).setStatus(status);
    }

    private Policy getPolicy(UUID id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found"));
    }

    private PolicyResponseDTO toResponse(Policy policy) {
        return new PolicyResponseDTO(
                policy.getId(),
                policy.getTitle(),
                policy.getVersion(),
                policy.getDescription(),
                policy.getContent(),
                policy.getStatus(),
                policy.getOwner() != null ? policy.getOwner().getId() : null,
                policy.getFramework() != null ? policy.getFramework().getId() : null,
                policy.getControls() != null
                        ? policy.getControls().stream().map(ComplianceControl::getId).collect(Collectors.toSet())
                        : Set.of()
        );
    }
}
