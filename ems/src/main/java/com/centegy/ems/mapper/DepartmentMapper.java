package com.centegy.ems.mapper;

import com.centegy.ems.dto.request.DepartmentRequestDTO;
import com.centegy.ems.dto.response.DepartmentResponseDTO;
import com.centegy.ems.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponseDTO toDepartmentResponseDTO(Department department) {
        if (department == null) return null;

        DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO();

        departmentResponseDTO.setId(department.getId());
        departmentResponseDTO.setName(department.getName());
        departmentResponseDTO.setHeadOfDepartmentId(department.getHeadOfDepartmentId());

        return departmentResponseDTO;

    }

    public Department toEntity(DepartmentRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        Department department = new Department();

        department.setName(requestDTO.getName());
        department.setHeadOfDepartmentId(requestDTO.getHeadOfDepartmentId());

        return department;
    }

}
