package com.centegy.ems.controller;

import com.centegy.ems.dto.request.DepartmentRequestDTO;
import com.centegy.ems.dto.response.DepartmentResponseDTO;
import com.centegy.ems.model.Department;
import com.centegy.ems.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DepartmentResponseDTO createDepartment(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        return departmentService.createDepartment(requestDTO);
    }

    @GetMapping
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentResponseDTO getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}