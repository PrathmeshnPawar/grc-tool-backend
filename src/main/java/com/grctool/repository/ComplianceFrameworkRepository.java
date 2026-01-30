package com.grctool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.ComplianceFramework;

public interface ComplianceFrameworkRepository
        extends JpaRepository<ComplianceFramework, UUID> {
}
