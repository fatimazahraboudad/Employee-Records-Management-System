package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.AuditLogDto;
import com.example.EmployeeRecordsManagementSystem.entities.AuditLog;

import java.util.List;

public interface AuditLogService {

    void save(AuditLog auditLog);
    List<AuditLogDto> getAllLog();
}