package com.example.EmployeeRecordsManagementSystem.dtos;

import com.example.EmployeeRecordsManagementSystem.enumeration.EmploymentStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto {


    private String idEmployee;

    @NotEmpty(message = "fullName should not be empty")
    @NotNull(message = "fullName should not be null")
    private String fullName;

    @Email(message = "email is not valid")
    private String email;

    @NotEmpty(message = "job Title should not be empty")
    @NotNull(message = "job Title should not be null")
    private String jobTitle;

    @NotEmpty(message = "department should not be empty")
    @NotNull(message = "department should not be null")
    private String department;

    private LocalDate hireDate;

    private EmploymentStatus employmentStatus;

    private String contactInformation;

    @NotEmpty(message = "address should not be empty")
    @NotNull(message = "address should not be null")
    private String address;
}
