package com.centegy.ems.mapper;

import com.centegy.ems.dto.request.EmployeeRequestDTO;
import com.centegy.ems.dto.response.EmployeeResponseDTO;
import com.centegy.ems.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    RoleMapper roleMapper;

    public EmployeeMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public EmployeeResponseDTO toEmployeeResponseDTO(Employee employee) {

        if (employee == null) return null;

        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();

        employeeResponseDTO.setFirstName(employee.getFirstName());
        employeeResponseDTO.setLastName(employee.getLastName());
        employeeResponseDTO.setEmail(employee.getEmail());
        employeeResponseDTO.setPhone(employee.getPhone());
        employeeResponseDTO.setEmploymentType(employee.getEmploymentType());

        if (employee.getManager() != null) {
            employeeResponseDTO.setManagerId(employee.getManager().getId());
        }

        employeeResponseDTO.setRoleId(roleMapper.roleResponseDTO(employee.getRole()));

        return employeeResponseDTO;

    }


    public Employee toEntity(EmployeeRequestDTO employeeRequestDTO) {

        if (employeeRequestDTO == null) return null;

        Employee employee = new Employee();

        employee.setFirstName(employeeRequestDTO.getFirstName());
        employee.setLastName(employeeRequestDTO.getLastName());
        employee.setEmail(employeeRequestDTO.getEmail());
        employee.setPhone(employeeRequestDTO.getPhone());
        employee.setEmploymentType(employeeRequestDTO.getEmploymentType());

        return employee;
    }

}
