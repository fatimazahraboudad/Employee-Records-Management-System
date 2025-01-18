package com.example.EmployeeRecordsManagementSystem.controllers;

import com.example.EmployeeRecordsManagementSystem.annotation.LogRequest;
import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @LogRequest(action = "Add new role")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @PostMapping("/add")
    public ResponseEntity<RoleDto> addNewRole(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
    }

    @LogRequest(action = "Fetch all role")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRole(), HttpStatus.OK);
    }

    @LogRequest(action = "Get role by id")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/{idRole}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable String idRole) {
        return new ResponseEntity<>(roleService.getRoleById(idRole), HttpStatus.OK);
    }

    @LogRequest(action = "Update role")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @PutMapping("/update")
    public ResponseEntity<RoleDto> updateRole( @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.updateRole(roleDto), HttpStatus.OK);
    }

    @LogRequest(action = "Delete role")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @DeleteMapping("delete/{idRole}")
    public ResponseEntity<String> deleteRole(@PathVariable String idRole) {
        return new ResponseEntity<>(roleService.deleteRole(idRole), HttpStatus.OK);
    }

    @LogRequest(action = "Get role by name")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("get/{name}")
    public ResponseEntity<RoleDto> getRoleByName(@PathVariable String name) {
        return new ResponseEntity<>(roleService.getRoleByName(name), HttpStatus.OK);
    }

}