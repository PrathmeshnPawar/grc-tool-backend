package com.grctool.model;

import com.grctool.enums.RiskCategory;
import com.grctool.enums.RiskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
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

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private RiskCategory category;

    @Min(1)
    @Max(5)
    private int impact; // 1–5

    @Min(1)
    @Max(5)
    private int likelihood; // 1–5

   
    private int riskScore; // impact * likelihood

    @Enumerated(EnumType.STRING)
    private RiskStatus status;

    @ManyToOne
    private User owner;

    @PostLoad
    @PostPersist
    @PreUpdate
    @PrePersist
    public int getRiskScore() {
        return this.riskScore = this.impact * this.likelihood;
    }
}
