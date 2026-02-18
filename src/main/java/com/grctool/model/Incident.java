package com.grctool.model;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.LastModifiedBy; // Use Set for ManyToMany/OneToMany relationships
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.grctool.enums.IncidentSeverity;
import com.grctool.enums.IncidentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class) // Enables @LastModifiedBy logic
public class Incident extends BaseEntity {

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    // Fixed: Typically an Incident affects multiple Risks, and a Risk can have multiple Incidents
    // Use @ManyToMany for the compliance loop mapping
    @ManyToMany
    @JoinTable(
        name = "incident_risks",
        joinColumns = @JoinColumn(name = "incident_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private Set<Risk> risks;

    // Senior Tip: Follow camelCase naming conventions (dateReported instead of DateReported) 
    private LocalDate dateReported;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_id", nullable = false)
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    @LastModifiedBy // Spring Data will handle this automatically
    private User updatedBy;

    /* WIZARD DELETE: 
       1. Remove the @ManyToOne LocalDateTime updatedAt. LocalDateTime is a data type, 
          not an entity, so it cannot have a @JoinColumn.
       2. BaseEntity already provides 'updatedAt' as a field. 
    */
}