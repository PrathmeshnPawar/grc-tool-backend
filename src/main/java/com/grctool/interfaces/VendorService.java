package com.grctool.interfaces;

import java.util.List;
import java.util.UUID;
import com.grctool.dto.vendor.VendorCreateDTO;
import com.grctool.dto.vendor.VendorResponseDTO;
import com.grctool.enums.VendorStatus;

public interface VendorService {
    VendorResponseDTO createVendor(VendorCreateDTO dto);
    VendorResponseDTO getVendorById(UUID id);
    List<VendorResponseDTO> getAllVendors();
    List<VendorResponseDTO> getVendorsByStatus(VendorStatus status);
    void deleteVendor(UUID id);
}