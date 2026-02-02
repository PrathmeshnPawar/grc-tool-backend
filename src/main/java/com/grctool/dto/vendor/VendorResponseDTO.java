package com.grctool.dto.vendor;

import java.util.UUID;

import com.grctool.enums.VendorStatus;
import com.grctool.enums.VendorTier;

public record VendorResponseDTO(
    UUID id,
    String name,
    String contactEmail,
    VendorStatus status,
    VendorTier tier
) {}