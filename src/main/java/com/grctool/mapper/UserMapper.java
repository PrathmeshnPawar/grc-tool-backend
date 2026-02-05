package com.grctool.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.enums.Permission_Name;
import com.grctool.model.Permissions;
import com.grctool.model.User;
import com.grctool.repository.PermissionsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PermissionsRepository permissionRepository;

    private final PasswordEncoder passwordEncoder;
   
        public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;
        Set<Permission_Name> permissionArray = (user.getPermissions() == null)
                ? java.util.Collections.emptySet()
                : user.getPermissions().stream()
                        .map(Permissions::getName)
                        .collect(Collectors.toSet()); // This creates a List<Permission_Name>
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                permissionArray);
    }

    public java.util.List<UserResponseDTO> toResponseDTOList(java.util.List<User> users) {
        if (users == null) return java.util.Collections.emptyList();
        return users.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;

       User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());

        // 3. SECURE THE PASSWORD (Assuming passwordEncoder is injected)
        user.setPassword(passwordEncoder.encode(dto.password())); // Temporary until you add Security

        user.setRole(dto.role());

        permissionRepository.findByName(Permission_Name.USER_READ).ifPresent(perm -> user.getPermissions().add(perm));

       return user;
    }
}
