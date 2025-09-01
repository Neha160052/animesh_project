package com.ttn.e_commerce_project.dto.vo;

import com.ttn.e_commerce_project.enums.Label;

public record SellerFlatVo(
        Long id,
        String fullName,
        String email,
        boolean isActive,
        String companyName,
        String companyContact,
        String city,
        String state,
        String country,
        String addressLine,
        int zipCode,
        Label label)
{}
