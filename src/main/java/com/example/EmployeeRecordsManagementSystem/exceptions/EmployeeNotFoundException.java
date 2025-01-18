package com.example.EmployeeRecordsManagementSystem.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String id) {
        super(ExceptionMessages.EMPLOYEE_NOT_FOUND.getMessage(id));
    }
}
