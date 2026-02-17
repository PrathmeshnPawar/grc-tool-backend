package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;

public interface PolicyService {

    PolicyResponseDTO createPolicy(PolicyCreateDTO dto);

    PolicyResponseDTO updatePolicy(UUID policyId, PolicyUpdateDTO dto);

    void changeStatus(UUID policyId, PolicyStatus status);

    List<PolicyResponseDTO> getAllPolicies();

    PolicyResponseDTO getPolicyById(UUID id);

    PolicyResponseDTO uploadPolicy(MultipartFile file, UUID ownerId, UUID frameworkId);

    void updatePolicyWithOcrResult(UUID policyId, String extractedContent); // For the background worker

}
