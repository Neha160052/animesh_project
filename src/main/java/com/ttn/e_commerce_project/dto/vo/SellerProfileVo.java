package com.ttn.e_commerce_project.dto.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class SellerProfileVo {

     Long id;
     String firstName;
     String lastName;
     boolean isActive;
     String companyContact;
     String companyName;
     String image;
     String gst;

     String city;
     String state;
     String country;
     String addressLine;
     int zipCode;

}
