package com.grctool.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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

    @Column(nullable=false)
    private String ipAddress;

    @Column(nullable=false)
    private String userAgent;
    
    @Column(nullable=false)// Stores device/browser info
    private String sessionId;

}
