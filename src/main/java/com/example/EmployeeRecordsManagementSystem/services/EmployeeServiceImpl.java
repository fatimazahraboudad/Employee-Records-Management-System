package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import com.example.EmployeeRecordsManagementSystem.entities.Employee;
import com.example.EmployeeRecordsManagementSystem.entities.User;
import com.example.EmployeeRecordsManagementSystem.exceptions.AlreadyExistException;
import com.example.EmployeeRecordsManagementSystem.exceptions.EmployeeNotFoundException;
import com.example.EmployeeRecordsManagementSystem.filters.EmployeeFilter;
import com.example.EmployeeRecordsManagementSystem.mappers.EmployeeMapper;
import com.example.EmployeeRecordsManagementSystem.repositories.EmployeeRepository;
import com.example.EmployeeRecordsManagementSystem.specifications.EmployeeSpecification;
import com.example.EmployeeRecordsManagementSystem.utilities.Variables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    @Override
    public List<EmployeeDto> getEmployees(EmployeeFilter filter) {
        UserDto userAuthenticated = userService.getCurrentUser();
        List<Employee> employeeList = null;
        if (userAuthenticated.getRole().size() == 1 && userAuthenticated.getRole().stream().anyMatch(role -> role.getName().equals(Variables.ROLE_MANAGER)))  {
            employeeList = employeeRepository.findByDepartmentIgnoreCase(getByEmail(userAuthenticated.getEmail()).getDepartment());
        } else {
            employeeList = employeeRepository.findAll(EmployeeSpecification.employeeSpecification(filter));
        }
        return employeeMapper.toDtos(employeeList);
    }

    @Override
    public EmployeeDto getEmployeeById(String id) {
        return employeeMapper.toDto(getById(id));
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        if(employeeRepository.findByEmailIgnoreCase(employeeDto.getEmail()).isPresent()){
            throw new AlreadyExistException(employeeDto.getEmail());
        }
        Employee employee = employeeMapper.toEntity(employeeDto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto editEmployee(EmployeeDto employeeDto) {
        Employee employee = getById(employeeDto.getIdEmployee());
        employee.setEmail(employeeDto.getEmail());
        employee.setAddress(employeeDto.getAddress());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setFullName(employeeDto.getFullName());
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setContactInformation(employeeDto.getContactInformation());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setEmploymentStatus(employeeDto.getEmploymentStatus());
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public String deleteEmployee(String id) {
        employeeRepository.delete(getById(id));
        return "employee deleted successfully";
    }

    public Employee getById(String id) {
        return employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("id "+id));
    }
    public Employee getByEmail(String email) {
        return employeeRepository.findByEmailIgnoreCase(email).orElseThrow(()->new EmployeeNotFoundException("email "+email));
    }
}
