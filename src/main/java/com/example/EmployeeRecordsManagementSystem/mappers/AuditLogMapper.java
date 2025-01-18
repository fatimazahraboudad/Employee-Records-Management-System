package com.example.EmployeeRecordsManagementSystem.mappers;
import com.example.EmployeeRecordsManagementSystem.dtos.AuditLogDto;
import com.example.EmployeeRecordsManagementSystem.entities.AuditLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLog toEntity(AuditLogDto auditLogDto);
    AuditLogDto toDto(AuditLog auditLog);
    List<AuditLogDto> toDtos(List<AuditLog> auditLogList);

}