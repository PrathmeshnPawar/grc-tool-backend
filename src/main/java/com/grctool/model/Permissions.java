package com.grctool.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permissions extends BaseEntity {
@Column(nullable = false, unique = true)
    String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users = new HashSet<>();
}
