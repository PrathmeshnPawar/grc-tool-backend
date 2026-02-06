package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;
import com.grctool.model.Permissions;
import com.grctool.model.User;

public interface PermissionService {
    List<Permissions> getAllPermissions();

    List<Permissions> addPermission(Permission_Name permissionName);

    // Adding this for the 'user' endpoint
    List<Permissions> getPermissionsByUserId(java.util.UUID userId);

    List<Permissions> getPermissionsByRole(Role roleName);

    User revokePermissionFromUser(UUID userId, Permission_Name permissionName);
}