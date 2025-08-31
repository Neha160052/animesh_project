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

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "password should have minimum 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Password must be 8-20 characters and include at least one digit, one uppercase, one lowercase, and one special character")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
