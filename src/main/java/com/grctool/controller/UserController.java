package com.grctool.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.enums.Permission_Name;
import com.grctool.interfaces.PermissionService;
import com.grctool.interfaces.UserService;
import com.grctool.mapper.UserMapper;
import com.grctool.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;
    private final UserMapper userMapper; // 1. Inject the Mapper

    // ---------------- ADMIN: CREATE USER ----------------

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUserByAdmin(@RequestBody UserRequestDTO dto) {
        // The service should ideally return a DTO, but if it returns an Entity:
        return userService.createUserByAdmin(dto); 
    }

    // ---------------- READ ----------------

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        // If userService.getAllUsers() returns List<User>, use the mapper:
        // return userMapper.toResponseDTOList(userService.getAllUsers());
        return userService.getAllUsers(); 
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // ---------------- PERMISSIONS ----------------

    @DeleteMapping("/{userId}/permissions/revoke")
    public ResponseEntity<UserResponseDTO> revokePermission(
            @PathVariable UUID userId, 
            @RequestParam Permission_Name permissionName) {
        
        // 1. Service Layer handles logic and returns the updated Entity
        User updatedUser = permissionService.revokePermissionFromUser(userId, permissionName);
        
        // 2. Use the INJECTED mapper, not a local helper
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }
}