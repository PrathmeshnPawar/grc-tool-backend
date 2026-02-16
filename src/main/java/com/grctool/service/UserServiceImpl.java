package com.grctool.service;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.enums.Permission_Name;
import com.grctool.enums.Role;
import com.grctool.exception.GrcSecurityException;
import com.grctool.exception.userException.UserAlreadyExistsException;
import com.grctool.interfaces.UserService;
import com.grctool.mapper.UserMapper;
import com.grctool.model.Permissions;
import com.grctool.model.User;
import com.grctool.repository.PermissionsRepository;
import com.grctool.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionsRepository permissionRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public UserResponseDTO createUserByAdmin(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException(userRequestDTO.email());
        }
        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public void grantPermission(UUID userId, String permissionName) {
        User user = userRepository.findById(userId).orElseThrow();
        Permission_Name enumName;
        try {
            enumName = Permission_Name.valueOf(permissionName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid permission: " + permissionName);
        }
        Permissions perm = permissionRepository
            .findByName(enumName)
            .orElseThrow(() ->
                new GrcSecurityException(
                    "Permission " +
                        enumName +
                        " is defined in Java but missing from Database. Run V13 migration!"
                )
            );
        user.getPermissions().add(perm);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void processOAuthPostLogin(String email, String name,String picture) {
        // 1. Check if the user already exists in our PostgreSQL database
        Optional<User> existUser = userRepository.findByEmail(email);

        if (existUser.isEmpty()) {
            // 2. If not, create a new User entity
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);

            // Defaulting to USER role for new SSO sign-ups for security
            // You can change this to Role.ADMIN if you are the first user
            newUser.setRole(Role.USER);
            newUser.setPicture(picture);

            // 3. Assign a default read permission so they can at least see the dashboard
            permissionRepository
                .findByName(Permission_Name.USER_READ)
                .ifPresent(perm -> newUser.getPermissions().add(perm));

            userRepository.save(newUser);
        } else {
        // Senior Tip: Update the picture in case the user changed it on Google
        User user = existUser.get();
        user.setPicture(picture);
        userRepository.save(user);
    }
        // Senior Tip: If the user exists, you could update their 'name'
        // or 'lastLogin' timestamp here for better auditing.
    }
}
