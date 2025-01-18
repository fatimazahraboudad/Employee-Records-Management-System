package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.filters.EmployeeFilter;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> getEmployees(EmployeeFilter filter);

    EmployeeDto getEmployeeById(String id);
    EmployeeDto addEmployee(EmployeeDto employee);
    EmployeeDto editEmployee(EmployeeDto employee);
    String deleteEmployee(String id);




}
