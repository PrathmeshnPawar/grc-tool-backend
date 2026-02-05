package com.grctool.model;

import java.util.HashSet;
import java.util.Set;

import com.grctool.enums.Permission_Name;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permissions extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true ,name="name")
    Permission_Name name;

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users = new HashSet<>();
}
