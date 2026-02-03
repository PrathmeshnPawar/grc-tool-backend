package com.grctool.dto.user;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
     //   String password,
        String role) {
};
