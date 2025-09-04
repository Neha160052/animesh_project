package com.ttn.e_commerce_project.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator  implements ConstraintValidator<ValidName, String> {

    // Regex: Only letters upper,lower, spaces allowed but not leading/trailing or multiple
    private static final String NAME_REGEX = "^[A-Za-z]+(?: [A-Za-z]+)*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return value.matches(NAME_REGEX);
    }
}
