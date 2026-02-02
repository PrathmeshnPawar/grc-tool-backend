package com.grctool.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.grctool.interfaces.UserService;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.dto.user.UserRequestDTO;
import com.grctool.model.User; // Use only your model
import com.grctool.repository.UserRepository; // Inject this!

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    // Inject the Repository, not the Service itself!
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(UUID.fromString(id));
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
public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
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
    user.setPassword(userRequestDTO.password()); // Temporary until you add Security
    
    user.setRole(userRequestDTO.role());

    // 4. Save and return the Response DTO
    User savedUser = userRepository.save(user);
    return toResponseDTO(savedUser);
}

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toResponseDTO(user);
    }

    // Helper method to convert Entity to DTO
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}