package com.example.EmployeeRecordsManagementSystem.services;

import com.example.EmployeeRecordsManagementSystem.dtos.RoleDto;
import com.example.EmployeeRecordsManagementSystem.entities.Role;
import com.example.EmployeeRecordsManagementSystem.exceptions.RoleAlreadyExist;
import com.example.EmployeeRecordsManagementSystem.exceptions.RoleNotFoundException;
import com.example.EmployeeRecordsManagementSystem.mappers.RoleMapper;
import com.example.EmployeeRecordsManagementSystem.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        if(roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RoleAlreadyExist(roleDto.getName());
        }
        Role role= roleMapper.toEntity(roleDto);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleDto getRoleById(String idRole) {
        return roleMapper.toDto(helper(idRole));
    }

    @Override
    public List<RoleDto> getAllRole() {
        return roleMapper.toDtos(roleRepository.findAll());
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Role role = helper(roleDto.getIdRole());
        role.setName(roleDto.getName());
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public String deleteRole(String idRole) {
        roleRepository.deleteById(idRole);
        return "role deleted successfully";
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);
        return roleMapper.toDto(role);
    }




    Role helper(String idRole) {
        return roleRepository.findById(idRole).orElseThrow( () -> new RoleNotFoundException(idRole));
    }
}
