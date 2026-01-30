package com.grctool.interfaces;

import java.util.UUID;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;

public interface PolicyService {

    PolicyResponseDTO createPolicy(PolicyCreateDTO dto);

    PolicyResponseDTO updatePolicy(UUID policyId, PolicyUpdateDTO dto);

    void changeStatus(UUID policyId, PolicyStatus status);
}
