package com.grctool.repository;

import java.util.UUID;
import com.grctool.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User  , UUID> {
    List<User> findByRole(com.grctool.enums.Role role);
}
