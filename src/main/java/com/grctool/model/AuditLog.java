package com.grctool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import jakarta.persistence.Column;

@Entity
@Getter
@Setter
public class AuditLog extends BaseEntity {

    @Column(nullable = false)
    private String entityName; // e.g., "Risk"

    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String action; // e.g., "UPDATE_SCORE"

    @Column(columnDefinition = "TEXT")
    private String changeDetails;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

}
