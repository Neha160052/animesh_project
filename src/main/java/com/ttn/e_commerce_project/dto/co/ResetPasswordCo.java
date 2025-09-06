package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordCo {

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    String email;

    @NotBlank(message = "{password.required}")
    @Size(min = 8, message = "{password.size}")
    @Pattern(regexp = PASSWORD_REGEX,
            message = "{password.pattern}")
    String password;

    @NotBlank(message = "{confirm.password.required}")
    String confirmPassword;
}
