package com.example.EmployeeRecordsManagementSystem.controllers;

import com.example.EmployeeRecordsManagementSystem.annotation.LogRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.JwtAuthenticationResponse;
import com.example.EmployeeRecordsManagementSystem.dtos.SignInRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.UserDto;
import com.example.EmployeeRecordsManagementSystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @LogRequest(action = "User register")
    @PostMapping("/users/register")
    public ResponseEntity<UserDto> addNewUser(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @LogRequest(action = "Fetch all users")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @LogRequest(action = "Get user by id")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/users/get/{idUser}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String idUser) {
        return new ResponseEntity<>(userService.getUserById(idUser), HttpStatus.OK);
    }

    @LogRequest(action = "Update profile")
    @PreAuthorize("hasRole(@R.ROLE_HR) or hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_MANAGER)")
    @PutMapping("/users/update")
    public ResponseEntity<UserDto> Update(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }


    @LogRequest(action = "login")
    @PostMapping("/users/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest singInd) throws Exception {
        return new ResponseEntity<>(userService.signIn(singInd), HttpStatus.OK);
    }

    @LogRequest(action = "logout")
    @GetMapping("/users/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.signOut(request, response);
        log.info("logout ");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }



    @PreAuthorize("hasRole(@R.ROLE_HR) or hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_MANAGER)")
    @GetMapping("/users/me")
    public ResponseEntity<UserDto> currentUser() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @LogRequest(action = "Add Authority to user")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/{idUser}/{name}")
    public ResponseEntity<UserDto> addAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(userService.addAuthority(idUser,name), HttpStatus.OK);
    }

    @LogRequest(action = "Remove Authority from user")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/remove/{idUser}/{name}")
    public ResponseEntity<UserDto> removeAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(userService.removeAuthority(idUser,name), HttpStatus.OK);
    }

    @LogRequest(action = "Delete user")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @DeleteMapping("/users/delete/{idUser}")
    public ResponseEntity<String> deleteUser(@PathVariable String idUser) {
        return new ResponseEntity<>(userService.deleteUser(idUser), HttpStatus.OK);
    }
}
