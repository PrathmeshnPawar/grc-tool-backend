package com.grctool.service;

import com.grctool.model.Risk;
import org.springframework.stereotype.Service;

@Service
public class RiskCalculatorService {

    public int calculateRiskScore(int impact, int likelihood) {
        return impact * likelihood;
    }

    public void calculateAndSetRiskScore(Risk risk) {
        int score = risk.getImpact() * risk.getLikelihood();
        risk.setRiskScore(score);
    }
}
