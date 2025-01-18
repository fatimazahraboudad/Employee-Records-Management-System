package com.example.EmployeeRecordsManagementSystem.mappers;

import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import com.example.EmployeeRecordsManagementSystem.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",  uses = RoleMapper.class)
public interface UserMapper {


    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    List<User> toEntities(List<UserDto> userDtoList);

    List<UserDto> toDtos(List<User> userList);

}