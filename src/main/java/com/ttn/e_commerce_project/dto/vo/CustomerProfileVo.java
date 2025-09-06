package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfileVo {
     Long id;
     String firstName;
     String lastName;
     boolean isActive;
     String contact;
     String image;
}
