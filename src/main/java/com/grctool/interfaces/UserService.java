    package com.grctool.interfaces;

    import java.util.List;
    import com.grctool.dto.user.UserResponseDTO;

    public interface  UserService {
        List<UserResponseDTO> getAllUsers();

        UserResponseDTO getUserById(String id);

        UserResponseDTO createUser(UserResponseDTO userResponseDTO);

        void deleteUser(String id);
    }
