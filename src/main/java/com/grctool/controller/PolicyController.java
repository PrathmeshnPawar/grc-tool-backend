package com.grctool.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.grctool.dto.policy.PolicyCreateDTO;
import com.grctool.dto.policy.PolicyResponseDTO;
import com.grctool.dto.policy.PolicyUpdateDTO;
import com.grctool.enums.PolicyStatus;
import com.grctool.interfaces.PolicyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Slf4j
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<PolicyResponseDTO> create(@RequestBody PolicyCreateDTO dto) {
        return new ResponseEntity<>(policyService.createPolicy(dto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PolicyResponseDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("owner_id") UUID ownerId,
            @RequestParam("framework_id") UUID frameworkId) {
        return ResponseEntity.accepted().body(policyService.uploadPolicy(file, ownerId, frameworkId));
    }

    // PolicyController.java
    @GetMapping(value = "/{id}/view", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> viewOriginalPdf(@PathVariable UUID id) {
        log.info("Request received to view PDF for policy ID: {}", id);

        PolicyResponseDTO policy = policyService.getPolicyById(id);

        if (policy.filePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get(policy.filePath()).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Senior Tip: We use a specific header builder to avoid any string typos
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + path.getFileName().toString() + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "no-cache") // Prevents browser from caching the 'gibberish'
                                                                       // version
                        .body(resource);
            } else {
                log.error("File path exists in DB but file is missing on disk: {}", policy.filePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Internal error streaming PDF: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponseDTO>> getAll() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponseDTO> update(@PathVariable UUID id, @RequestBody PolicyUpdateDTO dto) {
        return ResponseEntity.ok(policyService.updatePolicy(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestParam PolicyStatus status) {
        policyService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}