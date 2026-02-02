package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.incident.IncidentCreateDTO;
import com.grctool.dto.incident.IncidentResponseDTO;
import com.grctool.dto.incident.IncidentUpdateDTO;
import com.grctool.enums.IncidentStatus;

public interface IncidentService {

    IncidentResponseDTO createIncident(IncidentCreateDTO dto);

    IncidentResponseDTO updateIncident(UUID incidentId, IncidentUpdateDTO dto);

    void changeStatus(UUID incidentId, IncidentStatus status);

    IncidentResponseDTO getIncidentById(UUID incidentId);

    List<IncidentResponseDTO> getAllIncidents();
}
