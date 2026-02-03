package com.grctool.service;

import com.grctool.model.Risk;
import com.grctool.model.User;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.grctool.repository.RiskRepository;

@Service
public class RiskCalculatorService {

    private final AuditLogService auditLog;

    private final RiskRepository riskRepository;

    public RiskCalculatorService(RiskRepository riskRepository, AuditLogService auditLog) {
        this.riskRepository = riskRepository;
        this.auditLog = auditLog;
    }

    public Risk updateRiskAssessment(UUID id, int impact, int likelihood, User user) {
        Risk risk = riskRepository.findById(id).orElseThrow();

        int oldScore = risk.getRiskScore();
        int newScore = impact * likelihood; // Explicit calculation

        risk.setImpact(impact);
        risk.setLikelihood(likelihood);
        risk.setRiskScore(newScore);

        // Traceability: Compliance systems need to know WHY it changed
        auditLog.logChange(risk, "Score updated from " + oldScore + " to " + newScore, user);

        return riskRepository.save(risk);
    }

    public int calculateRiskScore(int impact, int likelihood) {
        return impact * likelihood;
    }

    public void calculateAndSetRiskScore(Risk risk) {
        int score = risk.getImpact() * risk.getLikelihood();
        risk.setRiskScore(score);
    }
}
