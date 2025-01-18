package com.example.EmployeeRecordsManagementSystem.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String idRole) {
        super(ExceptionMessages.ROLE_NOT_FOUND.getMessage(idRole));
    }

    public RoleNotFoundException() {
        super(ExceptionMessages.ROLE_NOT_FOUND_WITH_NAME.getMessage());
    }
}
