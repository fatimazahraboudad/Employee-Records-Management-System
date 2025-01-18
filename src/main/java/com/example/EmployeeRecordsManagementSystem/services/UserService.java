package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.SignInRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(String idUser);

    UserDto updateUser(UserDto userDto);


    JwtAuthenticationResponse signIn(SignInRequest request) throws Exception;
    void signOut(HttpServletRequest request, HttpServletResponse response);


    UserDto getCurrentUser();

    UserDto addAuthority(String idUser, String role);
    UserDto removeAuthority(String idUser, String role);

}