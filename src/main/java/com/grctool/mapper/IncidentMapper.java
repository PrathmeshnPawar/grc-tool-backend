package com.grctool.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.model.Incident;
import com.grctool.model.Risk;
import java.time.LocalDateTime;
import com.grctool.model.User;

@Component
public class IncidentMapper {

    LocalDateTime now = LocalDateTime.now();

    // INBOUND: DTO + User Entity -> New Incident Entity
    public Incident toEntity(IncidentCreateDTO dto, User reportedBy) {
        if (dto == null)
            return null;

        Incident incident = new Incident();
        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setStatus(IncidentStatus.OPEN);

        // WIZARD FIX: Calculate "now" inside the method call
        // Mapping a LocalDate (from DTO) to a LocalDateTime (in Entity)
        // Convert the current time to just the date
        incident.setDateReported(
                dto.dateReported() != null ? dto.dateReported() : java.time.LocalDate.now());

        incident.setReportedBy(reportedBy);

        return incident;
    }

    // OUTBOUND: Entity -> Response DTO
    public IncidentResponseDTO toResponse(Incident incident) {
        if (incident == null)
            return null;

        return new IncidentResponseDTO(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getDateReported(),
                incident.getReportedBy() != null ? incident.getReportedBy().getId() : null,
                incident.getRisks() != null
                        ? incident.getRisks().stream().map(Risk::getId).collect(Collectors.toSet())
                        : java.util.Set.of());
    }

    // WIZARD MOVE: The Bulk Translator
    public java.util.List<IncidentResponseDTO> toResponseList(java.util.List<Incident> incidents) {
        if (incidents == null || incidents.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return incidents.stream()
                .map(this::toResponse) // Reuses the single object mapper logic
                .toList(); // Java 16+ clean syntax
    }
}