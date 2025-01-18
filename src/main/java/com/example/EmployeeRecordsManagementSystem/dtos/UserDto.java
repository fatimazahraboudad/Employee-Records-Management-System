package com.example.EmployeeRecordsManagementSystem.dtos;

import com.example.EmployeeRecordsManagementSystem.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String idUser;
    @Email(message = "email is not valid")
    private String email;

    @ValidPassword
    private String password;

    private Set<RoleDto> role;


}
