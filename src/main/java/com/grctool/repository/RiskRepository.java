package com.grctool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.model.Risk;
import java.util.List;

public interface RiskRepository extends JpaRepository<Risk, UUID> {
    List<Risk> findByCategory(com.grctool.enums.RiskCategory category);
}
