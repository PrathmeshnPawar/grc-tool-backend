package com.grctool.model;

import com.grctool.enums.AuditResultStatus; // PASS, FAIL, INCONCLUSIVE

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "audit_result")
public class AuditResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false) // Mark as non-nullable
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id", nullable = false) // Mark as non-nullable
    private ComplianceControl control;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // ADD THIS: Explicitly mark as NOT NULL
    private AuditResultStatus status;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(nullable=false)
    private String evidenceUrl;
}