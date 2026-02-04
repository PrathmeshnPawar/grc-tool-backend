package com.grctool.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.grctool.enums.PolicyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Policy extends BaseEntity {

    private String title;
    private String version;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private User owner;

    @ManyToOne
    private ComplianceFramework framework;

    @ManyToMany
    @JoinTable(name = "policy_controls", joinColumns = @JoinColumn(name = "policy_id"), inverseJoinColumns = @JoinColumn(name = "control_id"))
    Set<ComplianceControl> controls;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}
