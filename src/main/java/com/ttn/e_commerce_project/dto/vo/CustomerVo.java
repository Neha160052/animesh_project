package com.ttn.e_commerce_project.dto.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerVo {
     Long id;
     String fullName;
     @Email(message = "Enter a valid email address")
     @NotBlank(message = "email is required")
     String email;
     boolean isActive;
}
