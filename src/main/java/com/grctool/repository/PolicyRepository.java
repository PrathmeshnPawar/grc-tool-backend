package com.grctool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.grctool.model.Policy;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy,UUID > {
    List<Policy> findByPolicyId(UUID id);

    List<Policy> findByStatus  (com.grctool.enums.PolicyStatus status);
}
