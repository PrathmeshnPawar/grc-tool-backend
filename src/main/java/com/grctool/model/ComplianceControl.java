package com.grctool.model;

import java.util.Set;

import com.grctool.enums.FrameworkStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ComplianceControl extends BaseEntity {

    private String name;
    private String controlCode;
    private String description;

    @Enumerated(EnumType.STRING)
    private FrameworkStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "framework_id")
    private ComplianceFramework framework;

    @ManyToMany
@JoinTable(
    name = "compliance_control_risks",
    joinColumns = @JoinColumn(name = "compliance_control_id"),
    inverseJoinColumns = @JoinColumn(name = "risk_id")
)
private Set<Risk> risks;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
}
