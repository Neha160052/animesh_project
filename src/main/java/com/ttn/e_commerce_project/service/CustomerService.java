package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.CustomerCo;

public interface CustomerService {

    void register(CustomerCo customerCo);
    CustomerProfileVo getMyProfile(String email);
    List<AddressVo> getMyAddresses(String email);
    String updateMyProfile(String email, CustomerProfileCo customerProfileCo);
    void updatePassword(String name, @Valid UpdatePasswordCo updatePasswordCo);
    String addCustomerAddress(String email, AddressCo addressCo);
    String deleteAddress(String email,Long id);
    String updateAddress(Long id, AddressCo addressCo);
}
