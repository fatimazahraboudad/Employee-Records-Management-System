package com.example.EmployeeRecordsManagementSystem.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private String idRole;

    @NotEmpty(message = "name of role should not be empty")
    @NotNull(message = "name of role should not be null")
    private String name;
}
