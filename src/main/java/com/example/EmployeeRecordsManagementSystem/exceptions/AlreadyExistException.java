package com.example.EmployeeRecordsManagementSystem.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String email) {
        super(ExceptionMessages.EMAIL_ALREADY_EXIST.getMessage(email));
    }
}
