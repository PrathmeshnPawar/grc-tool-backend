package com.grctool.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;
import com.grctool.interfaces.PolicyService;
import com.grctool.service.policyService.PolicyAutomationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final PolicyAutomationService policyAutomationService;

    // -------- CREATE POLICY --------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PolicyResponseDTO createPolicy(
            @RequestBody PolicyCreateDTO dto
    ) {
        return policyService.createPolicy(dto);
    }

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    //---------- GET ALL POLICIES --------
    @GetMapping
    public List<PolicyResponseDTO> getAllPolicies() {
        return policyService.getAllPolicies();
    }
    
    // -------- UPDATE POLICY --------
    @PutMapping("/{policyId}")
    public PolicyResponseDTO updatePolicy(
            @PathVariable UUID policyId,
            @RequestBody PolicyUpdateDTO dto
    ) {
        return policyService.updatePolicy(policyId, dto);
    }

    // -------- CHANGE STATUS --------
    @PatchMapping("/{policyId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(
            @PathVariable UUID policyId,
            @RequestParam PolicyStatus status
    ) {
        policyService.changeStatus(policyId, status);
    }

    @PostMapping("/automation/trigger-review-check")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void triggerAutomationCheck() {
        policyAutomationService.processOverduePolicyReviews();
    }
}
