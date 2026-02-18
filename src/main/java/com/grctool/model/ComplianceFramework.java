package com.grctool.model;

import java.util.Set;

import com.grctool.enums.FrameworkStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ComplianceFramework extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name; 

    private String version;

    @Column(columnDefinition = "TEXT")
    private String description;

    // --- NEW PROFESSIONAL METADATA ---
    @Enumerated(EnumType.STRING)
    private FrameworkStatus status = FrameworkStatus.DRAFT;

    private String category;       // e.g., "Information Security", "Privacy"
    
    private String regulatoryBody; // e.g., "ISO", "NIST", "RBI"

    private String referenceLink;  // URL to the official standard documentation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;            // The specific Compliance Officer responsible

    @OneToMany(mappedBy = "framework")
    private Set<Policy> policies;  // Inherited from your current model
    
    @OneToMany(mappedBy = "framework", cascade = CascadeType.ALL)
    private Set<ComplianceControl> controls;
}