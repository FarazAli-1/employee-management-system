package com.centegy.ems.mapper;

import com.centegy.ems.dto.request.RoleRequestDTO;
import com.centegy.ems.dto.response.RoleResponseDTO;
import com.centegy.ems.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    DepartmentMapper departmentMapper;

    public RoleMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }


    public RoleResponseDTO roleResponseDTO(Role role) {
        if (role == null) return null;

        RoleResponseDTO roleDTO = new RoleResponseDTO();

        roleDTO.setId(role.getId());
        roleDTO.setTitle(role.getTitle());
        roleDTO.setDepartment(departmentMapper.toDepartmentResponseDTO(role.getDepartment()));
        roleDTO.setRoleRank(role.getRoleLevel());

        return roleDTO;
    }

    public Role toEntity(RoleRequestDTO roleRequestDTO) {
        if (roleRequestDTO == null) return null;

        Role role = new Role();

        role.setRoleLevel(roleRequestDTO.getRoleRank());
        role.setTitle(roleRequestDTO.getTitle());

        return role;

    }

}