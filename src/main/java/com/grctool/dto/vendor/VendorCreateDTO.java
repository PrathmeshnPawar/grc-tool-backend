package com.grctool.dto.vendor;

import com.grctool.enums.VendorStatus;
import com.grctool.enums.VendorTier;

public record VendorCreateDTO(
    String name,
    String contactEmail,
    VendorStatus status,
    VendorTier tier
) {}