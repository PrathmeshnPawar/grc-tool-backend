package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.Incident;

public interface IncedentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByStatus(com.grctool.enums.IncidentStatus status);

    List<Incident> findBySeverity(com.grctool.enums.IncidentSeverity severity);
    
}
