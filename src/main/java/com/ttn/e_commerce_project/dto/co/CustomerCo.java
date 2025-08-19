package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class CustomerCo {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Phone number is required")
    @Min(value=10)
    @Max(value=10)
    long phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
    message = "Password must be 8-20 characters and include at least one digit, one uppercase, one lowercase, and one special character")
    String password;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;

    @NotBlank(message = "First name is required")
    String firstName;

    String middleName;
    @NotBlank(message = "Last name is required")
    String lastName;

    boolean isDeleted;
    boolean isActive;
    boolean isExpired;
    boolean isLocked;
    int invalidAttemptCount;
}
