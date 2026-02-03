package com.grctool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AuditLog extends BaseEntity {
    private String entityName; // e.g., "Risk"
    private UUID entityId;
    private String action;     // e.g., "UPDATE_SCORE"
    private String changeDetails;
    
    @ManyToOne
    private User performedBy;
}