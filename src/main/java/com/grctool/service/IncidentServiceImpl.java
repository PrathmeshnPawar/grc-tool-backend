package com.grctool.service;

import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.enums.Role;
import com.grctool.interfaces.IncidentService;
import com.grctool.mapper.IncidentMapper;
import com.grctool.model.Incident;
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
    private final IncidentMapper incidentMapper;

    @Transactional
    @Override
    public IncidentResponseDTO createIncident(IncidentCreateDTO dto) {
        User reportedBy = userRepository.findById(dto.reportedById())
                .orElseThrow(() -> new EntityNotFoundException("Reporting User not found"));
        
        Incident incident = incidentMapper.toEntity(dto, reportedBy);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponseDTO getIncidentById(UUID incidentId) {
        Incident incident = getIncident(incidentId);
        return incidentMapper.toResponse(incident);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponseDTO> getAllIncidents() {
        List<Incident> incidents = incidentRepository.findAll();
        // Use the mapper list method!
        return incidentMapper.toResponseList(incidents);
    }

    @Override
    @Transactional // WIZARD TIP: Mandatory for updates to persist
    public IncidentResponseDTO updateIncident(UUID incidentId, IncidentUpdateDTO dto) {
        Incident incident = getIncident(incidentId);
        
        // Authorization check
        User currentUser = dto.reportedBy(); 
        if (!incident.getReportedBy().getId().equals(currentUser.getId()) &&
                currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not authorized");
        }

        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setSeverity(dto.severity());
        incident.setDateReported(dto.dateReported());

        // Hibernate auto-saves here because of @Transactional
        return incidentMapper.toResponse(incident);
    }

    @Override
    @Transactional
    public void changeStatus(UUID incidentId, IncidentStatus status) {
        getIncident(incidentId).setStatus(status);
    }

    private Incident getIncident(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incident not found"));
    }
}