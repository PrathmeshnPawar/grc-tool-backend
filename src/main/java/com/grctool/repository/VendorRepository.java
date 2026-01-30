package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import com.grctool.model.Vendor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    List<Vendor> finVendorsByRiskCategory(com.grctool.enums.RiskCategory riskCategory);

    List<Vendor> findVendorTier(com.grctool.enums.VendorTier vendorTier);

    List<Vendor> findVendorStatus(com.grctool.enums.VendorStatus vendorStatus);
}
