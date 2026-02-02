package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.risk.RiskCreateDTO;
import com.grctool.dto.risk.RiskResponseDTO;
import com.grctool.dto.risk.RiskUpdateDTO;
import com.grctool.enums.RiskStatus;

public interface RiskService {

    RiskResponseDTO createRisk(RiskCreateDTO dto);

    RiskResponseDTO updateRisk(UUID riskId, RiskUpdateDTO dto);

    void assignOwner(UUID riskId, UUID userId);

    void linkIncident(UUID riskId, UUID incidentId);

    void changeStatus(UUID riskId, RiskStatus status);

    List<RiskResponseDTO> getHighRisks(int threshold);

    List<RiskResponseDTO> getAllRisks();
}
