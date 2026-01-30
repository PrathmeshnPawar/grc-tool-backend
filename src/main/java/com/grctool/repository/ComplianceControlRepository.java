package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.ComplianceControl;

public interface ComplianceControlRepository extends JpaRepository<UUID, ComplianceControl> {
    
    List<ComplianceControl> findByControlId(UUID frameworkId);

    List<ComplianceControl> findByComplianceStatus(com.grctool.enums.ComplianceStatus status);
}
