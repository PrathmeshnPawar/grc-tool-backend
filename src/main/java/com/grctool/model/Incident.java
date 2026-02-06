package com.grctool.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.LastModifiedBy;

import com.grctool.enums.IncidentSeverity;
import com.grctool.enums.IncidentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "incident")
    List<Risk> risks;

    private LocalDate DateReported;

    @ManyToOne
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    @LastModifiedBy
    private User updatedBy;

    @ManyToOne
    @JoinColumn(name = "updated_at")
    @LastModifiedBy
    private LocalDateTime updatedAt;
}
