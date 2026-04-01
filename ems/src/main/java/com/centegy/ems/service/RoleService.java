package com.centegy.ems.service;

import com.centegy.ems.model.Role;
import com.centegy.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final DepartmentService departmentService; // ADD THIS

    public RoleService(RoleRepository roleRepository, DepartmentService departmentService) {
        this.roleRepository = roleRepository;
        this.departmentService = departmentService;
    }

    public Role createRole(Role role) {
        departmentService.getDepartmentById(role.getDepartmentId());
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByID(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
    }
}