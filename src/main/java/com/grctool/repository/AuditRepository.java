package com.grctool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.Audit;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, UUID>{
    List<Audit> findByAuditId(UUID id);

    List<Audit> findByStatus(com.grctool.enums.AuditStatus auditStatus);
}
