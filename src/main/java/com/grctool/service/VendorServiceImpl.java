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
import com.grctool.mapper.VendorMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    @Transactional
    public VendorResponseDTO createVendor(VendorCreateDTO dto) {
        Vendor vendor = vendorRepository.save(vendorMapper.toEntity(dto));
        return vendorMapper.toResponse(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDTO getVendorById(UUID id) {
       Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vendor not found")); 
        return vendorMapper.toResponse(vendor);
                
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDTO> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendorMapper.toResponseList(vendors);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDTO> getVendorsByStatus(VendorStatus status) {
        List<Vendor> vendor = vendorRepository.findByStatus(status);
        return vendorMapper.toResponseList(vendor);
    }

    @Override
    public void deleteVendor(UUID id) {
        if (!vendorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendor not found");
        }
        vendorRepository.deleteById(id);
    }

}