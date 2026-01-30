package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.ComplianceControl;

public interface ComplianceControlRepository extends JpaRepository<ComplianceControl,UUID > {
    
    List<ComplianceControl> findByControlId(UUID id);

    List<ComplianceControl> findByStatus(com.grctool.enums.ComplianceStatus status);
}
