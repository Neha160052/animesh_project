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

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCo {

    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    @Pattern(regexp = EMAIL_REGEX, message = "{email.invalid}")
    String email;

    @NotBlank(message = "{phone.required}")
    @Pattern(
            regexp = PHONE_REGEX,
            message = "{phone.number.invalid}"
    )
    String phoneNumber;
    @NotBlank(message = "{password.required}")
    @Size(min = 8, message = "{password.size}")
    @Pattern( regexp = PASSWORD_REGEX,
              message = "{password.pattern}")
    String password;

    @NotBlank(message = "{confirm.password.required}")
    String confirmPassword;

    @NotBlank(message = "{first.name.required}")
    @ValidName(message = "{first.name.invalid}")
    String firstName;
    @ValidName(message = "{middle.name.invalid}")
    String middleName;

    @NotBlank(message = "{last.name.required}")
    @ValidName(message = "{last.name.invalid}")
    String lastName;

    boolean isDeleted;
    boolean isActive;
    boolean isExpired;
    boolean isLocked;
    int invalidAttemptCount;
}
