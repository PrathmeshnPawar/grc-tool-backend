package com.grctool.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;
import com.grctool.model.Permissions;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, UUID> {
    Optional<Permissions> findByName(Permission_Name permissionName);

@Query("SELECT DISTINCT p FROM Permissions p JOIN p.users u WHERE u.role = :role")
    List<Permissions> findByRoleName(@Param("role") Role role);

    // Direct User Lookup
    @Query("SELECT p FROM Permissions p JOIN p.users u WHERE u.id = :userId")
    List<Permissions> findByUserId(@Param("userId") UUID userId);
}
