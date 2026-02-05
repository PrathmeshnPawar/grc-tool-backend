package com.grctool.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grctool.enums.PolicyStatus;
import com.grctool.model.Policy;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {

    List<Policy> findByStatus(PolicyStatus status);

    List<Policy> findByOwner_Id(UUID ownerId);

    List<Policy> findByStatusInAndLastUpdatedBefore(Collection<PolicyStatus> statuses,
            java.time.LocalDateTime cutoffDate);

    @Query("SELECT p FROM Policy p WHERE p.status IN :statuses AND p.updatedAt < :cutoff")
    List<Policy> findStagnantPolicies(
            @Param("statuses") Collection<PolicyStatus> statuses,
            @Param("cutoff") LocalDateTime cutoff);

}
