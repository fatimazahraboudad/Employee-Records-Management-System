package com.example.EmployeeRecordsManagementSystem.controllers;

import com.example.EmployeeRecordsManagementSystem.annotation.LogRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.filters.EmployeeFilter;
import com.example.EmployeeRecordsManagementSystem.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @LogRequest(action = "Add new employee")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_HR)")
    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> addNewEmployee(@Validated @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.addEmployee(employeeDto), HttpStatus.CREATED);
    }

    @LogRequest(action = "Fetch all Employees")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_HR) or hasRole(@R.ROLE_MANAGER)")
    @PostMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestBody EmployeeFilter filter) {
        return new ResponseEntity<>(employeeService.getEmployees(filter), HttpStatus.OK);
    }

    @LogRequest(action = "Get employee by id")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_HR) or hasRole(@R.ROLE_MANAGER)")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @LogRequest(action = "Update employee")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_HR) or hasRole(@R.ROLE_MANAGER)")
    @PutMapping("/update")
    public ResponseEntity<EmployeeDto> updateEmployee(@Validated @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.editEmployee(employeeDto), HttpStatus.OK);
    }

    @LogRequest(action = "Delete employee")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_HR)")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }


}