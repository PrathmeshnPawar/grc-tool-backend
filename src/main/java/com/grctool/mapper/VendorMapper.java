package com.grctool.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.grctool.dto.vendor.VendorCreateDTO;
import com.grctool.dto.vendor.VendorResponseDTO;
import com.grctool.model.Vendor;

@Component
public class VendorMapper {

    // OUTBOUND: Entity -> Response (What the world sees)
    public VendorResponseDTO toResponse(Vendor v) {
        if (v == null) return null;
        return new VendorResponseDTO(
            v.getId(), v.getName(), v.getContactEmail(), v.getStatus(), v.getTier()
        );
    }

    // INBOUND: CreateDTO -> Entity (How we save to DB)
    public Vendor toEntity(VendorCreateDTO dto) {
        if (dto == null) return null;

        Vendor vendor = new Vendor();
        // Records use accessor methods like name() instead of getName()
        vendor.setName(dto.name()); 
        vendor.setContactEmail(dto.contactEmail());
        vendor.setStatus(dto.status());
        vendor.setTier(dto.tier());

        // Note: We don't set the ID here. 
        // Let the Database (@GeneratedValue) handle that.
        return vendor;
    }

    public List<VendorResponseDTO> toResponseList(List<Vendor> vendors) {   
        if (vendors == null) return java.util.Collections.emptyList();  
        return vendors.stream()
                .map(this::toResponse)
                .toList(); // Simplified for Java 16+
    }
}