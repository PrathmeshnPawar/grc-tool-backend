package com.grctool.dto.user;

import java.util.Set;
import java.util.UUID;

import com.grctool.enums.Permission_Name;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String role,
        Set<Permission_Name> permissions,
        String picture

) {
};
