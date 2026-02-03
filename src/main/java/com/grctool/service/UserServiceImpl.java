package com.grctool.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.user.UserRequestDTO; // Use only your model
import com.grctool.dto.user.UserResponseDTO; // Inject this!
import com.grctool.interfaces.UserService;
import com.grctool.model.User;
import com.grctool.repository.UserRepository;
import com.grctool.exception.userException.UserAlreadyExistsException;


import com.grctool.enums.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    // Inject the Repository, not the Service itself!
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO createUserByAdmin(UserRequestDTO userRequestDTO) {
        // 1. Check if user already exists
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // 2. Map DTO to Entity
        User user = new User();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());

        // 3. SECURE THE PASSWORD (Assuming passwordEncoder is injected)
        // user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setPassword(passwordEncoder.encode(userRequestDTO.password())); // Temporary until you add Security

        Role requestedRole = userRequestDTO.role();
        if (requestedRole == Role.AUDITOR ||
                requestedRole == Role.RISK_OWNER ||
                requestedRole == Role.USER) {

            user.setRole(requestedRole);

        } else {
            throw new IllegalArgumentException("Invalid role assignment by admin");
        }

        // 4. Save and return the Response DTO
        User savedUser = userRepository.save(user);
        // return toResponseDTO(savedUser);
        return toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        // 1. Check if user already exists
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException(userRequestDTO.email());

        }

        // 2. Map DTO to Entity
        User user = new User();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());

        // 3. SECURE THE PASSWORD (Assuming passwordEncoder is injected)
        user.setPassword(passwordEncoder.encode(userRequestDTO.password())); // Temporary until you add Security

        // 4. Assign default role
        user.setRole(Role.EMPLOYEE);

        // 5. Save and return the Response DTO
        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toResponseDTO(user);
    }

    // Helper method to convert Entity to DTO
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name());
    }

}