package com.grctool.model;

import java.time.Instant;

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
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id", nullable = false)
    private ComplianceControl control;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditResultStatus status;

    @Column(columnDefinition = "TEXT")
    private String findings;

    // ✅ NEW – matches migration
    @Column(name = "evidence_path", nullable = false)
    private String evidencePath;

    @Column(name = "evidence_type", length = 50)
    private String evidenceType;

    @Column(name = "evidence_checksum", length = 64)
    private String evidenceChecksum;

    @Column(name = "evidence_uploaded_at", nullable = false)
    private Instant evidenceUploadedAt;
}
