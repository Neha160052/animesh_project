package com.ttn.e_commerce_project.dto.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileVo {
     Long id;
     String firstName;
     String lastName;
     boolean isActive;
     String contact;
     String image;
}
