package com.grctool.model;

import java.util.Set;

import com.grctool.enums.ComplianceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ComplianceControl extends BaseEntity {

    private String controlCode;
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplianceStatus status;

    @ManyToMany
Set<Risk> risks;

    @ManyToOne
    private Vendor vendor;
}
