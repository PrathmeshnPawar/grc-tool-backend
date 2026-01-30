package com.grctool.model;

import com.grctool.enums.RiskCategory;
import com.grctool.enums.RiskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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

    private int impact;      // 1–5
    private int likelihood;  // 1–5
    private int riskScore;   // impact * likelihood

    @Enumerated(EnumType.STRING)
    private RiskStatus status;

    @ManyToOne
    private User owner;
}
