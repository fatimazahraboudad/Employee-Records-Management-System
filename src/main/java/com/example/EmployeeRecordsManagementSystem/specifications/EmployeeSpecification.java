package com.example.EmployeeRecordsManagementSystem.specifications;

import com.example.EmployeeRecordsManagementSystem.entities.Employee;
import com.example.EmployeeRecordsManagementSystem.filters.EmployeeFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeSpecification {
    public static Specification<Employee> employeeSpecification(EmployeeFilter filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filter.getDepartment() != null && !filter.getDepartment().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), "%" + filter.getDepartment().toLowerCase() + "%"));
            }
            if (filter.getEmploymentStatus() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("employmentStatus"), filter.getEmploymentStatus()));
            }
            if (filter.getHireDate() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("hireDate"), filter.getHireDate()));
            }
            return predicate;
        };
    }
}

