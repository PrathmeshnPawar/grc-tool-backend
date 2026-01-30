package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.enums.ComplianceStatus;
import com.grctool.model.ComplianceControl;

public interface ComplianceControlRepository extends JpaRepository<ComplianceControl, UUID> {

    List<ComplianceControl> findByStatus(ComplianceStatus status);

    List<ComplianceControl> findByFramework_Id(UUID frameworkId);
}
