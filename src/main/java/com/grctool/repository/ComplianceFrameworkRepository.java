package com.grctool.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.ComplianceFramework;

public interface ComplianceFrameworkRepository
        extends JpaRepository<ComplianceFramework, UUID> {
/**
     * Finds a framework by its exact name (e.g., "ISO 27001").
     */
    Optional<ComplianceFramework> findByName(String name);

    /**
     * Finds frameworks where the name contains a specific string, 
     * useful for search functionality.
     */
    List<ComplianceFramework> findByNameContainingIgnoreCase(String name);

    /**
     * Finds frameworks by version (e.g., "2022").
     */
    List<ComplianceFramework> findByVersion(String version);

    /**
     * Finds a framework by name and version to ensure 
     * you are targeting a specific iteration.
     */
    Optional<ComplianceFramework> findByNameAndVersion(String name, String version);


        
}
