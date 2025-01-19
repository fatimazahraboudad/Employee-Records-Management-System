package com.example.EmployeeRecordsManagementSystem.filters;

import com.example.EmployeeRecordsManagementSystem.enumeration.EmploymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeFilter {

    private String department;
    private EmploymentStatus employmentStatus;
    private LocalDate hireDate;


}
