package com.grctool.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grctool.dto.compliance.ComplianceControlCreateDTO;
import com.grctool.dto.compliance.ComplianceControlResponseDTO;
import com.grctool.enums.ComplianceStatus;
import com.grctool.interfaces.ComplianceControlService;



import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
public class ComplianceControlController {

    private final ComplianceControlService complianceService;

    // -------- CREATE CONTROL --------
    @PostMapping("/controls")
    @ResponseStatus(HttpStatus.CREATED)
    public ComplianceControlResponseDTO createControl(
            @RequestBody ComplianceControlCreateDTO dto
    ) {
        return complianceService.createControl(dto);
    }

    // -------- CHANGE STATUS --------
    @PatchMapping("/controls/{controlId}/status")
    public void changeControlStatus(
            @PathVariable UUID controlId,
            @RequestParam ComplianceStatus status
    ) {
        complianceService.changeControlStatus(controlId, status);
    }

    // -------- GET BY FRAMEWORK --------
    @GetMapping("/frameworks/{frameworkId}/controls")
    public List<ComplianceControlResponseDTO> getControlsByFramework(
            @PathVariable UUID frameworkId
    ) {
        return complianceService.getControlsByFramework(frameworkId);
    }

 
    
}
