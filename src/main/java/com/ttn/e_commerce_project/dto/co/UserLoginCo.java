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

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    String email;

    @NotBlank(message = "{password.required}")
    String password;
}
