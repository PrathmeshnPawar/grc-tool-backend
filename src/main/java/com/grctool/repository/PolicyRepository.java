package com.grctool.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grctool.enums.PolicyStatus;
import com.grctool.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, UUID> {

    List<Policy> findByStatus(PolicyStatus status);

    List<Policy> findByOwner_Id(UUID ownerId);

    List<Policy> findByStatusInAndLastUpdatedBefore(Collection<PolicyStatus> statuses, java.time.LocalDateTime cutoffDate);
}
