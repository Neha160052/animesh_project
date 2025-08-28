package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.SellerCo;

public interface SellerService {

    void register(SellerCo sellerCo);
    SellerProfileVo getSellerProfile(String email);
    void updateMyProfile(String email, @Valid SellerProfileCo req);
    void updatePassword(String username, @Valid UpdatePasswordCo updatePasswordCo);
    void updateAddress(String name, Long id, @Valid AddressCo addressCo);
}
