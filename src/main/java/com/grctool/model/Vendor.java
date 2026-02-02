package com.grctool.model;

import com.grctool.enums.VendorStatus;
import com.grctool.enums.VendorTier;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vendor extends BaseEntity {

    private String name;
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    private VendorStatus status;

    @Enumerated(EnumType.STRING)
    private VendorTier tier;
}
    