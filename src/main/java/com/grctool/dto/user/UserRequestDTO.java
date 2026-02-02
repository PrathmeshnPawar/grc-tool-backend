package com.grctool.dto.user;

import com.grctool.enums.Role;

public record UserRequestDTO(
    String name,
    String email,
    String password,
    Role role
) {}