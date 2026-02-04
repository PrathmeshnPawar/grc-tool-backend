package com.grctool.model;
import java.util.Set;
import java.util.HashSet;

import com.grctool.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    private String username;
    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
        name = "user_permissions",
        joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "permission_id")
    )
    private Set<Permissions> permissions = new HashSet<>();
}

