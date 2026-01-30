package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.enums.AuditStatus;
import com.grctool.model.Audit;

public interface AuditRepository extends JpaRepository<Audit, UUID> {

    List<Audit> findByStatus(AuditStatus status);

    List<Audit> findByAuditor_Id(UUID auditorId);

    List<Audit> findByRisk_Id(UUID riskId);
}
