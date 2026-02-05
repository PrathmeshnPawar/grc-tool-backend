package com.grctool.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.grctool.model.Permissions;
import com.grctool.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final PermissionService permissionService;
    private final UserService userService;

    // ---------------- ADMIN: CREATE USER WITH ROLE ----------------

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUserByAdmin(
            @RequestBody UserRequestDTO dto
    ) {
        return userService.createUserByAdmin(dto);
    }

    // ---------------- ADMIN: REGISTER EMPLOYEE ----------------

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO registerUser(
            @RequestBody UserRequestDTO dto
    ) {
        return userService.registerUser(dto);
    }

    // ---------------- READ ----------------

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // ---------------- DELETE ----------------

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

   @DeleteMapping("/{userId}/permissions/revoke")
    public ResponseEntity<UserResponseDTO> revokePermission(
            @PathVariable UUID userId, 
            @RequestParam Permission_Name permissionName) {
        
        // 1. Service Layer handles the business logic/transaction
        User updatedUser = permissionService.revokePermissionFromUser(userId, permissionName);
        
        // 2. Map to DTO using our helper
        return ResponseEntity.ok(mapToResponseDTO(updatedUser));
    }

    // WIZARD TIP: Centralized mapping logic
    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPermissions().stream()
                        .map(Permissions::getName)
                        .collect(Collectors.toSet())
        );
    }
}
