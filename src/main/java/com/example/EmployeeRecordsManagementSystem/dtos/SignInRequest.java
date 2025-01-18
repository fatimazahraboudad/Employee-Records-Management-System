package com.example.EmployeeRecordsManagementSystem.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @Email(message = "The email is not a valid email.")
    private String email;

    @NotBlank(message = "The password is required.")
    private String password;
}
