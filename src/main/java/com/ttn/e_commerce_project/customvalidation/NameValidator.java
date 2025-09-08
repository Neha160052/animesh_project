package com.ttn.e_commerce_project.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.ttn.e_commerce_project.constants.UserConstants.NAME_REGEX;

public class NameValidator  implements ConstraintValidator<ValidName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return value.matches(NAME_REGEX);
    }
}
