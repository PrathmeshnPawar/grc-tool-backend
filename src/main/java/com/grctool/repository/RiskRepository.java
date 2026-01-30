package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.enums.RiskCategory;
import com.grctool.enums.RiskStatus;
import com.grctool.model.Risk;

public interface RiskRepository extends JpaRepository<Risk, UUID> {

    List<Risk> findByCategory(RiskCategory category);

    List<Risk> findByStatus(RiskStatus status);

    List<Risk> findByOwner_Id(UUID ownerId);

    List<Risk> findByIncident_Id(UUID incidentId);

    List<Risk> findByRiskScoreGreaterThanEqual(int riskScore);
}
