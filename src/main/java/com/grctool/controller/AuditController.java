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

import com.grctool.dto.audit.AuditCreateDTO;
import com.grctool.dto.audit.AuditResponseDTO;
import com.grctool.dto.audit.AuditUpdateDTO;
import com.grctool.enums.AuditStatus;
import com.grctool.interfaces.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuditResponseDTO create(@RequestBody AuditCreateDTO dto) {
        return auditService.createAudit(dto);
    }

    @PutMapping("/{auditId}")
    public AuditResponseDTO update(
            @PathVariable UUID auditId,
            @RequestBody AuditUpdateDTO dto
    ) {
        return auditService.updateAudit(auditId, dto);
    }

    @PatchMapping("/{auditId}/status")
    public void changeStatus(
            @PathVariable UUID auditId,
            @RequestParam AuditStatus status
    ) {
        auditService.changeStatus(auditId, status);
    }

    @GetMapping("/status/{status}")
    public List<AuditResponseDTO> byStatus(@PathVariable AuditStatus status) {
        return auditService.getAuditsByStatus(status);
    }
}
