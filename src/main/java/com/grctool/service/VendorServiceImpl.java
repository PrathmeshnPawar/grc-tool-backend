package com.grctool.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grctool.dto.vendor.VendorCreateDTO;
import com.grctool.dto.vendor.VendorResponseDTO;
import com.grctool.enums.VendorStatus;
import com.grctool.interfaces.VendorService;
import com.grctool.model.Vendor;
import com.grctool.repository.VendorRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public VendorResponseDTO createVendor(VendorCreateDTO dto) {
        Vendor vendor = new Vendor();
        vendor.setName(dto.name());
        vendor.setContactEmail(dto.contactEmail());
        vendor.setStatus(dto.status());
        vendor.setTier(dto.tier());
        
        return toResponse(vendorRepository.save(vendor));
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDTO getVendorById(UUID id) {
        return vendorRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDTO> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deleteVendor(UUID id) {
        if (!vendorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendor not found");
        }
        vendorRepository.deleteById(id);
    }

    private VendorResponseDTO toResponse(Vendor v) {
        return new VendorResponseDTO(
                v.getId(),
                v.getName(),
                v.getContactEmail(),
                v.getStatus(),
                v.getTier()
        );
    }
}