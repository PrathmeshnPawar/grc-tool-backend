package com.grctool.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.interfaces.IncidentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    // -------- CREATE INCIDENT --------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponseDTO createIncident(
            @RequestBody IncidentCreateDTO dto
    ) {
        return incidentService.createIncident(dto);
    }

    // -------- UPDATE INCIDENT --------
    @PutMapping("/{incidentId}")
    public IncidentResponseDTO updateIncident(
            @PathVariable UUID incidentId,
            @RequestBody IncidentUpdateDTO dto
    ) {
        return incidentService.updateIncident(incidentId, dto);
    }

    // -------- CHANGE STATUS --------
    @PatchMapping("/{incidentId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(
            @PathVariable UUID incidentId,
            @RequestParam IncidentStatus status
    ) {
        incidentService.changeStatus(incidentId, status);
    }
}
