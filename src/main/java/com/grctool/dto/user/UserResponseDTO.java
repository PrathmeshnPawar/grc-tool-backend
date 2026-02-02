package com.grctool.dto.user;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String role) {
};
