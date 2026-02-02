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

import com.grctool.dto.risk.RiskCreateDTO;
import com.grctool.dto.risk.RiskResponseDTO;
import com.grctool.dto.risk.RiskUpdateDTO;
import com.grctool.enums.RiskStatus;
import com.grctool.interfaces.RiskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/risks")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RiskResponseDTO create(@RequestBody RiskCreateDTO dto) {
        return riskService.createRisk(dto);
    }

    @GetMapping
    public List<RiskResponseDTO> getAllRisk() {
        return  riskService.getAllRisks();
    }
    

    @PutMapping("/{riskId}")
    public RiskResponseDTO update(
            @PathVariable UUID riskId,
            @RequestBody RiskUpdateDTO dto
    ) {
        return riskService.updateRisk(riskId, dto);
    }

    @PatchMapping("/{riskId}/status")
    public void changeStatus(
            @PathVariable UUID riskId,
            @RequestParam RiskStatus status
    ) {
        riskService.changeStatus(riskId, status);
    }

    @GetMapping("/high")
    public List<RiskResponseDTO> getHighRisks(
            @RequestParam int threshold
    ) {
        return riskService.getHighRisks(threshold);
    }
}
