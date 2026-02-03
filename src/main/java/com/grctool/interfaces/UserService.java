package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;

import com.grctool.dto.user.UserRequestDTO;
import com.grctool.dto.user.UserResponseDTO;


public interface UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(UUID id);

  //  UserResponseDTO User(UserRequestDTO userRequestDTO);

    void deleteUser(UUID id);

    UserResponseDTO createUserByAdmin(UserRequestDTO userRequestDTO);

    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
}
