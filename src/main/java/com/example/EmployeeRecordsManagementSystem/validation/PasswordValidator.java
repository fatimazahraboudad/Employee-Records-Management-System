package com.example.EmployeeRecordsManagementSystem.validation;

import com.example.EmployeeRecordsManagementSystem.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        return (password.chars().anyMatch(ch -> "!@#$%^&*()-_+=<>?/".indexOf(ch) >= 0)) &&
                (password.chars().anyMatch(Character::isUpperCase)) &&
                (password.chars().anyMatch(Character::isLowerCase)) &&
                (password.chars().anyMatch(Character::isDigit));
    }
}