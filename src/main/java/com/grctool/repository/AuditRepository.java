package com.grctool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.Audit;

import java.util.List;

public interface AuditRepository extends JpaRepository<UUID, Audit>{
    List<Audit> findByEntityId(UUID entityId);

    List<Audit> findByAuditStatus(com.grctool.enums.AuditStatus auditStatus);
}
