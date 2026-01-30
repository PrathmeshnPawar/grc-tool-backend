package com.grctool.model;

import com.grctool.enums.PolicyStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Policy extends BaseEntity {

    private String title;
    private String version;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    @Lob
    private String content;

    @ManyToOne
    private User owner;

    @ManyToOne
    private ComplianceFramework framework;
}
