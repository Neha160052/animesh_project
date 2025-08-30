package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.CustomerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerProfileVo;
import jakarta.validation.Valid;

import java.util.List;

public interface CustomerService {

    void register(CustomerCo customerCo);
    CustomerProfileVo getMyProfile(String email);
    List<AddressVo> getMyAddresses(String email);
    String updateMyProfile(String email, CustomerProfileCo customerProfileCo);
    void updatePassword(String name, @Valid UpdatePasswordCo updatePasswordCo);
    String addCustomerAddress(String email, AddressCo addressCo);
    String deleteAddress(String email,Long id);
    String updateAddress(Long id, AddressCo addressCo);
    void checkOwnership(Long id, String email);
}
