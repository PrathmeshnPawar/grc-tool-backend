package com.grctool.service.policyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;
import com.grctool.interfaces.PolicyService;
import com.grctool.mapper.PolicyMapper;
import com.grctool.model.Policy;
import com.grctool.repository.ComplianceControlRepository;
import com.grctool.repository.ComplianceFrameworkRepository;
import com.grctool.repository.PolicyRepository;
import com.grctool.repository.UserRepository;
import com.grctool.service.OCRService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional // Ensures atomicity for DB operations
@Slf4j
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceControlRepository controlRepository;
    private final PolicyMapper policyMapper;
    private final OCRService ocrService;

    // Define the upload path for your Ubuntu Server [cite: 2026-02-05]
    private final String uploadPath = "/var/lib/grctool/policies/";

    @Override
    public PolicyResponseDTO createPolicy(PolicyCreateDTO dto) {
        log.info("Creating new manual policy: {}", dto.title());
        Policy policy = new Policy();
        policy.setTitle(dto.title());
        policy.setVersion(dto.version());
        policy.setDescription(dto.description());
        policy.setContent(dto.content());
        policy.setStatus(PolicyStatus.DRAFT);

        policy.setOwner(userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found")));

        policy.setFramework(frameworkRepository.findById(dto.frameworkId())
                .orElseThrow(() -> new EntityNotFoundException("Framework not found")));

        if (dto.controlIds() != null) {
            policy.setControls(new HashSet<>(controlRepository.findAllById(dto.controlIds())));
        }

        Policy saved = policyRepository.save(policy);
        return policyMapper.toResponse(saved);
    }

    @Override
    public PolicyResponseDTO uploadPolicy(MultipartFile file, UUID ownerId, UUID frameworkId) {
        log.info("Starting file upload and OCR process for: {}", file.getOriginalFilename());

        // 1. Save file to Ubuntu local storage
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = Paths.get(uploadPath).resolve(fileName);

        try {
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage());
            throw new RuntimeException("Could not store file", e);
        }

        // 2. Create PENDING policy shell
        Policy policy = new Policy();
        policy.setTitle("PENDING OCR: " + file.getOriginalFilename());
        policy.setStatus(PolicyStatus.DRAFT);
        policy.setContent("Extracting data from document... please wait.");
        policy.setFilePath(targetLocation.toString());

        policy.setOwner(userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found")));
        policy.setFramework(frameworkRepository.findById(frameworkId)
                .orElseThrow(() -> new EntityNotFoundException("Framework not found")));

        Policy saved = policyRepository.save(policy);

        // 3. Trigger Background OCR (Fire and Forget)
        ocrService.extractTextFromPdf(saved.getId(), targetLocation.toString());

        return policyMapper.toResponse(saved);
    }

    @Override
    public PolicyResponseDTO updatePolicy(UUID policyId, PolicyUpdateDTO dto) {
        Policy policy = getPolicy(policyId);
        policy.setTitle(dto.title());
        policy.setVersion(dto.version());
        policy.setDescription(dto.description());
        policy.setContent(dto.content());

        if (dto.frameworkId() != null) {
            policy.setFramework(frameworkRepository.findById(dto.frameworkId())
                    .orElseThrow(() -> new EntityNotFoundException("Framework not found")));
        }

        if (dto.controlIds() != null) {
            policy.setControls(new HashSet<>(controlRepository.findAllById(dto.controlIds())));
        }

        return policyMapper.toResponse(policy);
    }

@Override
@Transactional
public void updatePolicyWithOcrResult(UUID policyId, String extractedContent) {
    Policy policy = getPolicy(policyId);
    policy.setContent(extractedContent);
    policy.setProcessed(true); // Ensure this is set to true!
    
    // Senior Tip: Clean up the "PENDING" title so the UI looks professional
    if (policy.getTitle().startsWith("PENDING OCR: ")) {
        policy.setTitle(policy.getTitle().replace("PENDING OCR: ", ""));
    }
    
    policyRepository.save(policy);
    log.info("Policy {} is now fully processed and title cleaned.", policyId);
}

    @Override
    public List<PolicyResponseDTO> getAllPolicies() {
        return policyMapper.toResponseList(policyRepository.findAll());
    }

    @Override
    public PolicyResponseDTO getPolicyById(UUID id) {
        return policyMapper.toResponse(getPolicy(id));
    }

    @Override
    public void changeStatus(UUID policyId, PolicyStatus status) {
        getPolicy(policyId).setStatus(status);
    }

    private Policy getPolicy(UUID id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found"));
    }
}