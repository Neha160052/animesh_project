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

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class SellerCo {


    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.required}")
    @Pattern(regexp = EMAIL_REGEX,message = "{email.invalid}")
    String email;

    @NotBlank(message = "{password.required}")
    @Pattern(regexp = PASSWORD_REGEX,
                    message = "{password.pattern}")
    String password;

    @NotBlank(message = "{confirm.password.required}")
    String confirmPassword;

    @Pattern(regexp = GST_REGEX,
            message = "{gst.invalid}")
    String gst;

    @NotBlank(message = "{company.name.required}")
    @ValidName(message = "{company.name.invalid}")
    String companyName;

    @Valid
    @NotNull(message = "{company.address.required}")
    AddressCo companyAddress;

    @Pattern(regexp = COMPANY_CONTACT, message = "{company.contact.invalid}")
    String companyContact;

    @NotBlank(message = "{first.name.required}")
    @ValidName(message = "{first.name.invalid}")
    String firstName;

    @NotBlank(message = "{last.name.required}")
    @ValidName(message="{last.name.invalid}")
    String lastName;

    @ValidName(message="{middle.name.invalid}")
    String middleName;
}
