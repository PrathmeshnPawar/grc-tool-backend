package com.grctool.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;
import com.grctool.enums.Role;
import com.grctool.exception.userException.AccessDeniedException;
import com.grctool.interfaces.IncidentService;
import com.grctool.mapper.IncidentMapper;
import com.grctool.model.Incident;
import com.grctool.model.User;
import com.grctool.repository.IncidentRepository;
import com.grctool.repository.UserRepository;

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

        // 1. Capture the saved entity in a variable named 'saved'
        Incident saved = incidentRepository.save(incident);

        // 2. Perform the logging BEFORE the return
       

        // 3. Finally, map the 'saved' variable to the response
        return incidentMapper.toResponse(saved);
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
@Transactional 
public IncidentResponseDTO updateIncident(UUID incidentId, IncidentUpdateDTO dto) {
    Incident incident = getIncident(incidentId);
    
    // 1. Identify the actor (The one sending the request)
    User actor = dto.reportedBy(); // In the future, this comes from SecurityContext
    
    // 2. Authorization Check
    if (!incident.getReportedBy().getId().equals(actor.getId()) &&
            actor.getRole() != Role.ADMIN) {
        throw new AccessDeniedException("Not authorized to edit this incident");
    }

    // 3. Map the data changes
    incidentMapper.updateEntityFromDto(dto, incident);

    // 4. Capture the "Who" and "When"
    incident.setUpdatedBy(actor);
    incident.setUpdatedAt(LocalDateTime.now());

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