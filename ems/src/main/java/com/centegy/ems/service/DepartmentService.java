package com.centegy.ems.service;

import com.centegy.ems.model.Department;
import com.centegy.ems.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
    }

    public void deleteDepartment(Integer id) {
        boolean isDeleted = departmentRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Department not found with ID: " + id);
        }
    }
}