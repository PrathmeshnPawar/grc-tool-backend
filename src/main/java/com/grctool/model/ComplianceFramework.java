package com.grctool.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String name;   // ISO 27001, SOC 2

    private String description;

    private String version;
    
    @OneToMany(mappedBy = "framework")
    private Set<Policy> policies;

}
