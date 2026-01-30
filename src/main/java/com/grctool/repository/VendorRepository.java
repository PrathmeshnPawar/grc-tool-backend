package com.grctool.repository;

import java.util.List;
import java.util.UUID;

import com.grctool.model.Vendor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor,UUID > {
    List<Vendor> findByRiskStatus(com.grctool.enums.RiskCategory riskCategory);

    List<Vendor> findByTier(com.grctool.enums.VendorTier vendorTier);

    List<Vendor> findByStatus(com.grctool.enums.VendorStatus vendorStatus);

    List<Vendor> findVendorById(Vendor id);
}
