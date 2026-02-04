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
import com.grctool.enums.IncidentSeverity;
import com.grctool.enums.IncidentStatus;
import com.grctool.enums.Role;
import com.grctool.interfaces.IncidentService;
import com.grctool.model.Incident;
import com.grctool.model.Risk;
import com.grctool.model.User;
import com.grctool.repository.IncidentRepository;
import com.grctool.repository.UserRepository;
import com.grctool.exception.userException.AccessDeniedException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public IncidentResponseDTO createIncident(IncidentCreateDTO dto) {

        User reportedBy = userRepository.findById(dto.reportedById())
                .orElseThrow(() -> new EntityNotFoundException("Reporting User not found"));
        Incident incident = new Incident();
        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setStatus(IncidentStatus.OPEN);
        incident.setDateReported(dto.dateReported());

        incident.setReportedBy(reportedBy);

        if (dto.severity() == IncidentSeverity.CRITICAL) {
            reportedBy.setRole(Role.ADMIN);
            userRepository.save(reportedBy); // explicit persistence
        }

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
        User currentUser = dto.reportedBy();
        if (!incident.getReportedBy().getId().equals(currentUser.getId()) &&
                currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not authorized");
        }
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
                        : Set.of());
    }
}
