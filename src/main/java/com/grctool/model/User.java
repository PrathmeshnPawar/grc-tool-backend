package com.grctool.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set; // For senior-level equals/hashCode

import com.grctool.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Required for JPA
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    // We keep this but make it nullable because SSO users won't have a local password
    private String password;

    private String picture;

    // The unique ID from your SSO provider (e.g., Google 'sub' claim)
    @Column(name = "sso_id", unique = true)
    private String ssoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE; // Default to EMPLOYEE for security

    // Senior Move: Use FetchType.LAZY to avoid performance hits 
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permissions> permissions = new HashSet<>();

    // Senior Tip: When using Sets with JPA, always implement equals and hashCode 
    // based on a unique business key (like email) to avoid collection bugs 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}