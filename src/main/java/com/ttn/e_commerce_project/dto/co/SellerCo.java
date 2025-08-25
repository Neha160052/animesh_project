package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class SellerCo {


    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
                    message = "Password must be 8-20 characters and include at least one digit, one uppercase, one lowercase, and one special character")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9]{1}$",
            message = "Invalid GST format")
    private String gst;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @Valid
    @NotNull(message = "Company address is required")
    private AddressCo companyAddress;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid company contact number")
    private String companyContact;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String middleName;
}

