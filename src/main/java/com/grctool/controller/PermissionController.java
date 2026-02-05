package com.grctool.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;
import com.grctool.interfaces.PermissionService;
import com.grctool.model.Permissions;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor // Wizard Tip: Cleans up the constructor injection
public class PermissionController {

    private final PermissionService permissionService;

    // 1. Get all available permissions in the system
    @GetMapping
    public ResponseEntity<List<Permissions>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    // 2. Get permissions for a specific user
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<Permissions>> getPermissionsByRole(@PathVariable Role roleName) {
        return ResponseEntity.ok(permissionService.getPermissionsByRole(roleName));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Permissions>> getPermissionsByUserId(@PathVariable java.util.UUID userId) {
        return ResponseEntity.ok(permissionService.getPermissionsByUserId(userId));
    }

    // 3. Add a new permission to the system
    @PostMapping("/add")
    public ResponseEntity<List<Permissions>> addPermission(@RequestParam Permission_Name name) {
        return ResponseEntity.ok(permissionService.addPermission(name));
    }
}