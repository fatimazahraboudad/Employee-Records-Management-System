package com.example.EmployeeRecordsManagementSystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    USER_NOT_FOUND("User with id %s not found."),
    EMAIL_ALREADY_EXIST("mail %s already exists."),
    EMAIL_OR_PASSWORD_INVALID("email pr password invalid."),
    TOKEN_EXPIRED("Token expired, verification invalid."),
    OUPS_SOMETHING_WRONG("Oups something wrong!"),
    USER_ALREADY_HAVE_ROLE("user with id %s already have %s role"),
    ROLE_NOT_FOUND("Role with id %s not found."),
    ROLE_NOT_FOUND_WITH_NAME("Role not found."),
    ROLE_ALREADY_EXIST("Role with name %s already exist."),
    EMPLOYEE_NOT_FOUND("Employee with %s not found.");


    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}