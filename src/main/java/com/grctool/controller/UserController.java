package com.grctool.controller;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.enums.Permission_Name;
import com.grctool.interfaces.PermissionService;
import com.grctool.interfaces.UserService;
import com.grctool.mapper.UserMapper;
import com.grctool.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;
    private final UserMapper userMapper;

    // ---------------- AUTHENTICATION & SESSION ----------------

    /**
     * The "Wizard" Endpoint:
     * This is what your Next.js UserContext calls to verify the session.
     * It extracts user details directly from the secure Google OIDC token.
     */
    @GetMapping("/v1/auth/me")
    public ResponseEntity<?> getCurrentUser(
        @AuthenticationPrincipal OidcUser oidcUser
    ) {
        if (oidcUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Senior Tip: Eventually, you should fetch the user from your DB by email
        // to return their assigned internal Role and Permissions.
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", oidcUser.getEmail());
        userDetails.put("name", oidcUser.getFullName());
        userDetails.put("picture", oidcUser.getPicture());
        userDetails.put("role", "ADMIN"); // Placeholder for DB-driven role mapping

        return ResponseEntity.ok(userDetails);
    }

    // Note: Manual @PostMapping("/login") is removed.
    // OAuth2Login handles the login flow automatically.

    // ---------------- ADMIN: USER MANAGEMENT ----------------

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUserByAdmin(@RequestBody UserRequestDTO dto) {
        return userService.createUserByAdmin(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
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
        @RequestParam Permission_Name permissionName
    ) {
        User updatedUser = permissionService.revokePermissionFromUser(
            userId,
            permissionName
        );
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }
}
