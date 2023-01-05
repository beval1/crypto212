package com.crypto212.auth.util.validators.impl;

import com.crypto212.auth.util.validators.UserUsernameValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.crypto212.auth.config.AppConstants.MAXIMUM_USERNAME_LENGTH;
import static com.crypto212.auth.config.AppConstants.MINIMUM_USERNAME_LENGTH;

public class UserUsernameValidatorImpl implements ConstraintValidator<UserUsernameValidator, String> {

    @Override
    public void initialize(UserUsernameValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regex = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        return value != null && value.length() >= MINIMUM_USERNAME_LENGTH && value.length() <= MAXIMUM_USERNAME_LENGTH
                && value.matches(regex);
    }
}
