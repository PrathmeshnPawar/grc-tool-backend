package com.grctool.model;

import com.grctool.enums.ComplianceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

@Entity
public class ComplianceControl extends BaseEntity {

    private String controlCode;
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplianceStatus status;

    @ManyToOne
    private ComplianceFramework framework;
}
