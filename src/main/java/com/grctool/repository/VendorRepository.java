package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grctool.enums.VendorStatus;
import com.grctool.enums.VendorTier;
import com.grctool.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    List<Vendor> findByStatus(VendorStatus status);

    List<Vendor> findByName(String name);

    List<Vendor> findByContactEmail(String contactEmail);

    Vendor findByNameAndContactEmail(String name, String contactEmail);

    // Removed the typo method; use the built-in findById() instead
    
    List<Vendor> findByTier(VendorTier tier);
}