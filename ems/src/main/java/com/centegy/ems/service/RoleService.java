package com.centegy.ems.service;

import com.centegy.ems.dto.request.RoleRequestDTO;
import com.centegy.ems.dto.response.RoleResponseDTO;
import com.centegy.ems.mapper.RoleMapper;
import com.centegy.ems.model.Department;
import com.centegy.ems.model.Role;
import com.centegy.ems.repository.DepartmentRepository;
import com.centegy.ems.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final DepartmentRepository departmentRepository;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, DepartmentRepository departmentRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.departmentRepository = departmentRepository;
    }

    public RoleResponseDTO createRole(RoleRequestDTO requestDTO) {

            Department realDepartment = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId()));

            Role role = roleMapper.toEntity(requestDTO);

            role.setDepartment(realDepartment);

            Role savedRole = roleRepository.save(role);
            return  roleMapper.roleResponseDTO(savedRole);

    }

    public List<RoleResponseDTO> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::roleResponseDTO).toList();
    }

    public RoleResponseDTO getRoleByID(Long id) {

        Role role = roleRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));

        return roleMapper.roleResponseDTO(role);
    }

}