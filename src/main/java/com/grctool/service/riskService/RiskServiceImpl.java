package com.grctool.service.riskService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.risk.RiskCreateDTO;
import com.grctool.dto.risk.RiskResponseDTO;
import com.grctool.dto.risk.RiskUpdateDTO;
import com.grctool.enums.RiskStatus;
import com.grctool.interfaces.RiskService;
import com.grctool.model.Incident;
import com.grctool.model.Risk;
import com.grctool.model.User;
import com.grctool.repository.IncidentRepository;
import com.grctool.repository.RiskRepository;
import com.grctool.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RiskServiceImpl implements RiskService {

    private final RiskRepository riskRepository;
    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;
    private final RiskCalculatorService riskCalculator;

    // ---------------- CREATE ----------------

    @Override
    public RiskResponseDTO createRisk(RiskCreateDTO dto) {

        Risk risk = new Risk();
        risk.setTitle(dto.title());
        risk.setDescription(dto.description());
        risk.setCategory(dto.category());
        risk.setImpact(dto.impact());
        risk.setLikelihood(dto.likelihood());
        risk.setStatus(RiskStatus.OPEN);

        // ⭐ calculate risk score
        riskCalculator.calculateAndSetRiskScore(risk);

        if (dto.ownerId() != null) {
            User owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
            risk.setOwner(owner);
        }

        if (dto.incidentId() != null) {
            Incident incident = incidentRepository.findById(dto.incidentId())
                    .orElseThrow(() -> new EntityNotFoundException("Incident not found"));
            risk.setIncident(incident);
        }

        return toResponse(riskRepository.save(risk));
    }
    // ---------------- READ ALL RISKS ----------------

    @Override
    @Transactional(readOnly = true)
    public List<RiskResponseDTO> getAllRisks() {
        return riskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------- UPDATE ----------------

    @Override
    public RiskResponseDTO updateRisk(UUID riskId, RiskUpdateDTO dto) {
        Risk risk = getRisk(riskId);

        risk.setTitle(dto.title());
        risk.setDescription(dto.description());
        risk.setCategory(dto.category());
        risk.setImpact(dto.impact());
        risk.setLikelihood(dto.likelihood());

        // ⭐ recalculate after update
        riskCalculator.calculateAndSetRiskScore(risk);

        return toResponse(risk);
    }

    // ---------------- RELATIONS ----------------

    @Override
    public void assignOwner(UUID riskId, UUID userId) {
        Risk risk = getRisk(riskId);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        risk.setOwner(owner);
    }

    @Override
    public void linkIncident(UUID riskId, UUID incidentId) {
        Risk risk = getRisk(riskId);

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new EntityNotFoundException("Incident not found"));

        risk.setIncident(incident);
    }

    // ---------------- STATE ----------------

    @Override
    public void changeStatus(UUID riskId, RiskStatus status) {
        Risk risk = getRisk(riskId);
        risk.setStatus(status);
    }

    // ---------------- READ ----------------

    @Override
    @Transactional(readOnly = true)
    public List<RiskResponseDTO> getHighRisks(int threshold) {
        return riskRepository.findByRiskScoreGreaterThanEqual(threshold)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------- HELPERS ----------------

    private Risk getRisk(UUID id) {
        return riskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Risk not found"));
    }

    private RiskResponseDTO toResponse(Risk risk) {
        return new RiskResponseDTO(
                risk.getId(),
                risk.getTitle(),
                risk.getDescription(),
                risk.getCategory(),
                risk.getImpact(),
                risk.getLikelihood(),
                risk.getRiskScore(),
                risk.getStatus(),
                risk.getCreatedAt(),
                risk.getOwner() != null ? risk.getOwner().getId() : null,
                risk.getIncident() != null ? risk.getIncident().getId() : null);
    }
}
