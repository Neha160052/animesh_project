package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
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


    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.required}")
    private String email;

    @NotBlank(message = "{password.required}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
                    message = "{password.pattern}")
    private String password;

    @NotBlank(message = "{confirm.password.required}")
    private String confirmPassword;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9]{1}$",
            message = "{gst.invalid}")
    private String gst;

    @NotBlank(message = "{company.name.required}")
    @ValidName(message = "{company.name.invalid}")
    private String companyName;

    @Valid
    @NotNull(message = "{company.address.required}")
    private AddressCo companyAddress;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "{company.contact.invalid}")
    private String companyContact;

    @NotBlank(message = "{first.name.required}")
    @ValidName(message = "{first.name.invalid}")
    private String firstName;

    @NotBlank(message = "{last.name.required}")
    @ValidName(message="{last.name.invalid}")
    private String lastName;

    private String middleName;
}
