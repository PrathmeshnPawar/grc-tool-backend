package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;
import com.grctool.exception.GrcSecurityException;
import com.grctool.interfaces.PermissionService;
import com.grctool.model.Permissions;
import com.grctool.model.User;
import com.grctool.repository.PermissionsRepository;
import com.grctool.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionsRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Permissions> getAllPermissions() {
        List<Permissions> permissions = permissionRepository.findAll();
        if (permissions.isEmpty()) {
            throw new GrcSecurityException("No permissions found. Ensure seed data is loaded.");
        }
        return permissions;
    }

    @Override
    public List<Permissions> getPermissionsByRole(Role roleName) {
            // WIZARD MOVE: Use a specific query instead of findAll()
            return permissionRepository.findByRoleName(roleName);
       
    }

    @Override
    @Transactional
    public List<Permissions> addPermission(Permission_Name permissionName) {
        // Use ifPresentOrElse style or simply check presence to keep it readable
        if (permissionRepository.findByName(permissionName).isEmpty()) {
            Permissions newPermission = new Permissions();
            newPermission.setName(permissionName);
            permissionRepository.save(newPermission);
        }
        return getAllPermissions();
    }

    @Override
    public List<Permissions> getPermissionsByUserId(UUID userId) {
        List<Permissions> userPermissions = permissionRepository.findByUserId(userId);
        if (userPermissions.isEmpty()) {
            // Senior Tip: Don't always throw exceptions here.
            // A new user might simply have 0 permissions. Return empty list instead.
            return java.util.Collections.emptyList();
        }
        return userPermissions;
    }

    @Override
    @Transactional
    public User revokePermissionFromUser(UUID userId, Permission_Name permissionName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GrcSecurityException("User not found"));

        Permissions permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new GrcSecurityException("Permission not found"));

        // WIZARD MOVE: Remove from both sides to ensure consistency
        user.getPermissions().remove(permission);
        permission.getUsers().remove(user);

         return userRepository.save(user); // JPA handles the join table deletion
    }
}