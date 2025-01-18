package com.example.EmployeeRecordsManagementSystem.mappers;

import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.entities.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    List<RoleDto> toDtos(List<Role> roleList);


}
