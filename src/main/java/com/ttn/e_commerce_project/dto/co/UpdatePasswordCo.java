package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class UpdatePasswordCo {
    @NotBlank(message = "{current.password.required}")
    private String currentPassword;

    @NotBlank(message = "{new.password.required}")
    @Size(min = 8, message = "{new.password.size}")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "{new.password.pattern}"
    )
    private String newPassword;

    @NotBlank(message = "{confirm.password.required}")
    private String confirmPassword;
}
