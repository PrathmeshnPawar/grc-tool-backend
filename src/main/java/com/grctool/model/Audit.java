package com.grctool.model;

import java.time.LocalDate;

import com.grctool.enums.AuditStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends BaseEntity {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne
    private User LeadAuditor;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;
}
