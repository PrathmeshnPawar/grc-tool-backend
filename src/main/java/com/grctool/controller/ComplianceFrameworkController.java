package com.grctool.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grctool.dto.compliance.ComplianceFrameworkCreateDTO;
import com.grctool.dto.compliance.ComplianceFrameworkResponseDTO;
import com.grctool.interfaces.ComplianceFrameworkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compliance/frameworks")
@RequiredArgsConstructor
public class ComplianceFrameworkController {

    private final ComplianceFrameworkService frameworkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComplianceFrameworkResponseDTO create(@RequestBody ComplianceFrameworkCreateDTO dto) {
        return frameworkService.createFramework(dto);
    }

    @GetMapping
    public List<ComplianceFrameworkResponseDTO> getAll() {
        return frameworkService.getAllFrameworks();
    }

    @GetMapping("/{id}")
    public ComplianceFrameworkResponseDTO getById(@PathVariable UUID id) {
        return frameworkService.getFrameworkById(id);
    }

    // grctool/controller/ComplianceFrameworkController.java

@PutMapping("/{id}")
public ComplianceFrameworkResponseDTO update(
        @PathVariable UUID id, 
        @RequestBody ComplianceFrameworkCreateDTO dto // Reusing CreateDTO for simplicity or create a specific UpdateDTO
) {
    return frameworkService.updateFramework(id, dto);
}
}