package com.grctool.model;

import java.time.LocalDate;
import java.util.Set;

import com.grctool.enums.AuditStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends BaseEntity {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne
    private User leadAuditor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Risk risk;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    @ManyToMany
    @JoinTable(
        name = "audit_tested_controls",
        joinColumns = @JoinColumn(name = "audit_id"),
        inverseJoinColumns = @JoinColumn(name = "control_id")
    )
    private Set<ComplianceControl> testedControls;

    @ManyToMany
    @JoinTable(
        name = "audit_reviewed_policies",
        joinColumns = @JoinColumn(name = "audit_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private Set<Policy> reviewedPolicies;
}
