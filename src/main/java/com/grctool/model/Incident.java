package com.grctool.model;

import java.time.LocalDate;

import com.grctool.enums.IncidentSeverity;
import com.grctool.enums.IncidentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Incident extends BaseEntity {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    private LocalDate DateReported ;

    @ManyToOne
    private User reportedBy;
}
