package com.centegy.ems.service;

import com.centegy.ems.dto.request.DepartmentRequestDTO;
import com.centegy.ems.dto.response.DepartmentResponseDTO;
import com.centegy.ems.mapper.DepartmentMapper;
import com.centegy.ems.model.Department;
import com.centegy.ems.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {

        Department department =  departmentMapper.toEntity(departmentRequestDTO);

        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toResponseDTO(savedDepartment);

    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departmentResponseDTOS =  departmentRepository.findAll();

        return departmentResponseDTOS.stream().map(departmentMapper::toResponseDTO).toList();
    }

    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        return departmentMapper.toResponseDTO(department);
    }

    public void deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Department not found with ID: " + id);
        }

        departmentRepository.deleteById(id);
    }
}