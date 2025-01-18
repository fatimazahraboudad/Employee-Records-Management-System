package com.example.EmployeeRecordsManagementSystem.repositories;

import com.example.EmployeeRecordsManagementSystem.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,String>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByEmailIgnoreCase(String email);
    List<Employee> findByDepartmentIgnoreCase(String department);


}
