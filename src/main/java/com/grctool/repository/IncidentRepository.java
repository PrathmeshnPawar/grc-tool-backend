package com.grctool.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grctool.model.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByStatus(com.grctool.enums.IncidentStatus status);

    List<Incident> findBySeverity(com.grctool.enums.IncidentSeverity severity);

    List<Incident> findByReportedBy_Id(UUID id);

    List<Incident> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
}
