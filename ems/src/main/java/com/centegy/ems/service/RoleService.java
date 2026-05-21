package com.centegy.ems.service;

import com.centegy.ems.dto.request.RoleRequestDTO;
import com.centegy.ems.dto.response.RoleResponseDTO;
import com.centegy.ems.exceptions.ResourceNotFoundException;
import com.centegy.ems.mapper.RoleMapper;
import com.centegy.ems.model.Department;
import com.centegy.ems.model.Role;
import com.centegy.ems.repository.DepartmentRepository;
import com.centegy.ems.repository.RoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "roles",  allEntries = true)
    public RoleResponseDTO createRole(RoleRequestDTO requestDTO) {

            Department realDepartment = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + requestDTO.getDepartmentId()));

            Role role = roleMapper.toEntity(requestDTO);

            role.setDepartment(realDepartment);

            Role savedRole = roleRepository.save(role);
            return  roleMapper.roleResponseDTO(savedRole);

    }

    @Cacheable(value = "roles")
    public List<RoleResponseDTO> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::roleResponseDTO).collect(java.util.stream.Collectors.toList());
    }

    @Cacheable(value="role", key="#id")
    public RoleResponseDTO getRoleByID(Long id) {

        Role role = roleRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));

        return roleMapper.roleResponseDTO(role);
    }

    @CacheEvict(value = "roles", allEntries = true)
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }

}