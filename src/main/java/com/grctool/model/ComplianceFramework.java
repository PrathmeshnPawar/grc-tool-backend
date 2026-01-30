package com.grctool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ComplianceFramework extends BaseEntity {

    private String name;   // ISO 27001, SOC 2
    private String description;

    
    @ManyToOne
    private Policy policy;

    @ManyToOne
    private ComplianceControl control;
}
