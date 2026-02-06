package com.grctool.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.model.Incident;
import com.grctool.model.Risk;
import com.grctool.model.User;

@Component
public class IncidentMapper {

    // INBOUND: DTO + User Entity -> New Incident Entity
    public Incident toEntity(IncidentCreateDTO dto, User reportedBy) {
        if (dto == null)
            return null;

        Incident incident = new Incident();
        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setStatus(IncidentStatus.OPEN);

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

    // INBOUND: Update existing Incident Entity from Update DTO
    // WIZARD TIP: This pattern is called "Partial Mapping" or "Update Mapping"
    public void updateEntityFromDto(IncidentUpdateDTO dto, Incident incident) {
        if (dto == null || incident == null)
            return;

        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());

        // Only update the date if the DTO actually provides one
        if (dto.dateReported() != null) {
            incident.setDateReported(dto.dateReported());
        }

        // NOTE: We usually don't allow changing the 'reportedBy' user
        // during an update for audit trail integrity.
    }
}
