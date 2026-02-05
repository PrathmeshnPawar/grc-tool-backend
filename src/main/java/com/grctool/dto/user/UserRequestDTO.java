package com.grctool.dto.user;

import com.grctool.enums.Role;
import com.grctool.enums.Permission_Name;
import java.util.List;

public record UserRequestDTO(
    String name,
    String email,
    String password,
    Role role,
    List<Permission_Name> permissions
) {}