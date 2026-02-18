package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grctool.enums.FrameworkStatus;
import com.grctool.model.ComplianceControl;
import com.grctool.model.ComplianceFramework;

@Repository
public interface ComplianceControlRepository extends JpaRepository<ComplianceControl, UUID> {

    List<ComplianceControl> findByStatus(FrameworkStatus status);

    List<ComplianceControl> findByFramework_Id(UUID frameworkId);

    List<ComplianceControl> findByFramework(ComplianceFramework framework);
}
