package com.grctool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.Risk;
import java.util.List;

public interface RiskRepository extends JpaRepository<Risk,UUID > {
    List<Risk> findByCategory(com.grctool.enums.RiskCategory riskCategory);

    List<Risk> findRiskById(UUID id);

    List<Risk> findByStatus(com.grctool.enums.RiskStatus riskStatus);

    List<Risk> findBySeverity(com.grctool.enums.RiskSeverity riskSeverity);

    // Find all risks associated with a specific incident to show in a 'linked items' view
List<Risk> findByIncident_Id(UUID incidentId);
}
