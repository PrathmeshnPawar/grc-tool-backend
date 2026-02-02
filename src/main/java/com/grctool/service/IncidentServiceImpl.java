package com.grctool.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.interfaces.IncidentService;
import com.grctool.model.Incident;
import com.grctool.model.Risk;
import com.grctool.repository.IncidentRepository;
import com.grctool.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Override
    public IncidentResponseDTO createIncident(IncidentCreateDTO dto) {

        Incident incident = new Incident();
        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setStatus(IncidentStatus.OPEN);
        incident.setDateReported(dto.dateReported());

        incident.setReportedBy(
                userRepository.findById(dto.reportedById())
                        .orElseThrow(() -> new EntityNotFoundException("Reporter not found"))
        );

        return toResponse(incidentRepository.save(incident));
    }

    @Override
    public IncidentResponseDTO getIncidentById(UUID incidentId) {
        Incident incident = getIncident(incidentId);
        return toResponse(incident);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponseDTO> getAllIncidents() {
        return incidentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public IncidentResponseDTO updateIncident(UUID incidentId, IncidentUpdateDTO dto) {
        Incident incident = getIncident(incidentId);

        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setDateReported(dto.dateReported());

        return toResponse(incident);
    }

    @Override
    public void changeStatus(UUID incidentId, IncidentStatus status) {
        getIncident(incidentId).setStatus(status);
    }

    private Incident getIncident(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incident not found"));
    }

    private IncidentResponseDTO toResponse(Incident incident) {
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
                        : Set.of()
        );
    }
}
