package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grctool.model.AuditResult;

@Repository
public interface AuditResultRepository extends JpaRepository<AuditResult, UUID> {
    
    // Find all results for a specific audit
    List<AuditResult> findByAuditId(UUID auditId);
    
    // Find the result for a specific control within an audit
    List<AuditResult> findByAuditIdAndControlId(UUID auditId, UUID controlId);
}