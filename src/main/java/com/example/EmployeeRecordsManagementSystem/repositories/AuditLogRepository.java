package com.example.EmployeeRecordsManagementSystem.repositories;

import com.example.EmployeeRecordsManagementSystem.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}