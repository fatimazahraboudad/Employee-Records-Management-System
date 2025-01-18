package com.example.EmployeeRecordsManagementSystem.controllers;
import com.example.EmployeeRecordsManagementSystem.annotation.LogRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.AuditLogDto;
import com.example.EmployeeRecordsManagementSystem.services.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuditLogController {

    private final AuditLogService auditLogService;


    @LogRequest(action = "Fetch all logs")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/logs")
    public ResponseEntity<List<AuditLogDto>> getAllLogs() {
        return new ResponseEntity<>(auditLogService.getAllLog(), HttpStatus.OK);
    }
}