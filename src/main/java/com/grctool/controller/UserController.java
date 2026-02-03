package com.grctool.controller;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;
import com.grctool.interfaces.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ---------------- ADMIN: CREATE USER WITH ROLE ----------------

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUserByAdmin(
            @RequestBody UserRequestDTO dto
    ) {
        return userService.createUserByAdmin(dto);
    }

    // ---------------- ADMIN: REGISTER EMPLOYEE ----------------

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO registerUser(
            @RequestBody UserRequestDTO dto
    ) {
        return userService.registerUser(dto);
    }

    // ---------------- READ ----------------

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // ---------------- DELETE ----------------

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
