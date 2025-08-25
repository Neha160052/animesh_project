package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Getter
@RequiredArgsConstructor
public class UserLoginCo {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Password is required")
    String password;
}
