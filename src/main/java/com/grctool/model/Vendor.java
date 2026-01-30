package com.grctool.model;

import java.util.Set;

import com.grctool.enums.RiskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vendor extends BaseEntity {

    private String name;
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    private RiskStatus riskStatus;

    @OneToMany
Set<Risk> risks;

@OneToMany
Set<Incident> incidents;
}
    