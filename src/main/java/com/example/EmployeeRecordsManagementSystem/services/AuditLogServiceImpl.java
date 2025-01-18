package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.AuditLogDto;
import com.example.EmployeeRecordsManagementSystem.entities.AuditLog;
import com.example.EmployeeRecordsManagementSystem.entities.Role;
import com.example.EmployeeRecordsManagementSystem.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void save(AuditLog auditLog) {
        auditLog.setId(UUID.randomUUID().toString());

        auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogDto> getAllLog() {
        return auditLogRepository.findAll().stream()
                .map(auditLog ->
                        AuditLogDto.builder()
                                .date(auditLog.getDate())
                                .action(auditLog.getAction())
                                .details(auditLog.getDetails())
                                .email(auditLog.getUser() != null ?auditLog.getUser().getEmail():"unknown email")
                                .role(auditLog.getUser() != null ?auditLog.getUser().getRole().stream().map(Role::getName).toList():new ArrayList<>())
                                .address(auditLog.getAddress())
                                .ipAddress(auditLog.getIpAddress())
                                .build()


                ).toList();
    }
}