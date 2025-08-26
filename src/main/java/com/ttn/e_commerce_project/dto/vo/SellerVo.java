package com.ttn.e_commerce_project.dto.vo;

import com.ttn.e_commerce_project.entity.address.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerVo {
    Long id;
    String fullName;
    String email;
    boolean isActive;
    String companyName;
    String companyContact;
    AddressVo address;
}
