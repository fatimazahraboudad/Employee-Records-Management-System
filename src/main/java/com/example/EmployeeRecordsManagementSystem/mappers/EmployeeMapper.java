package com.example.EmployeeRecordsManagementSystem.mappers;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.entities.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    EmployeeDto toDto(Employee employee);

    Employee toEntity(EmployeeDto employeeDto);

    List<EmployeeDto> toDtos(List<Employee> employees);


}
