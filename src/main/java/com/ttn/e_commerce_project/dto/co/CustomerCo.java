package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
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
@FieldDefaults(level= AccessLevel.PRIVATE)
public class CustomerCo {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank
    @Size(min=10,max=10)
    String  phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
    message = "Password must be 8-20 characters and include at least one digit, one uppercase, one lowercase, and one special character")
    String password;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;

    @NotBlank(message = "First name is required")
    @ValidName(message = "firstName can only contain alphabets and spaces")
    String firstName;

    @ValidName(message = "middleName can only contain alphabets and spaces")
    String middleName;

    @ValidName(message = "Lastname can only contain alphabets and spaces")
    @NotBlank(message = "Last name is required")
    String lastName;

    boolean isDeleted;
    boolean isActive;
    boolean isExpired;
    boolean isLocked;
    int invalidAttemptCount;
}
