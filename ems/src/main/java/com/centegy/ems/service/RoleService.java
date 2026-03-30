package com.centegy.ems.service;


import com.centegy.ems.model.Role;
import com.centegy.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByID(Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
    }


}
