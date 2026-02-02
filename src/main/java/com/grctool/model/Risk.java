package com.grctool.model;

import java.util.Set;

import com.grctool.enums.RiskCategory;
import com.grctool.enums.RiskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private RiskCategory category;

    @Min(1)
    @Max(5)
    private int impact;

    @Min(1)
    @Max(5)
    private int likelihood;

    private int riskScore;

    @Enumerated(EnumType.STRING)
    private RiskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Incident incident;

@ManyToMany(mappedBy = "risks")
private Set<ComplianceControl> controls;


    @PrePersist
    @PreUpdate
    private void calculateRiskScore() {
        this.riskScore = this.impact * this.likelihood;
    }
}
