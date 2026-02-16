package com.grctool.dto.user;

import java.util.List;

import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;

public record UserRequestDTO(
    String name,
    String email,
    String password,
    Role role,
    List<Permission_Name> permissions,
    String picture
) {}