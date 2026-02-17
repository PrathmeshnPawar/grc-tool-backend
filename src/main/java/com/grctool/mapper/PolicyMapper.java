package com.grctool.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.model.ComplianceControl;
import com.grctool.model.Policy;

@Component
public class PolicyMapper {

    public PolicyResponseDTO toResponse(Policy policy) {
        if (policy == null) return null;

        return new PolicyResponseDTO(
            policy.getId(),
            policy.getTitle(),
            policy.getVersion(),
            policy.getDescription(),
            policy.getContent(),
            policy.getStatus(),
            policy.getOwner() != null ? policy.getOwner().getId() : null,
            policy.getFramework() != null
                ? policy.getFramework().getId()
                : null,
            mapControlsToIds(policy.getControls()),
            policy.getCreatedAt(),
            policy.getUpdatedAt(),
            policy.getFilePath(),
            policy.isProcessed()
        );
    }

    // Wizard Tip: Extract complex collection mapping to private methods
    // to keep the main toResponse method readable.
    private Set<java.util.UUID> mapControlsToIds(
        Set<ComplianceControl> controls
    ) {
        if (controls == null || controls.isEmpty()) {
            return Collections.emptySet();
        }
        return controls
            .stream()
            .map(ComplianceControl::getId)
            .collect(Collectors.toSet());
    }

    public List<PolicyResponseDTO> toResponseList(List<Policy> policies) {
        if (policies == null) return Collections.emptyList();
        return policies.stream().map(this::toResponse).toList();
    }
}
