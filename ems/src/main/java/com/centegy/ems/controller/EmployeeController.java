package com.centegy.ems.controller;


import com.centegy.ems.dto.request.EmployeeRequestDTO;
import com.centegy.ems.dto.response.EmployeeResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Employee;
import com.centegy.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.addEmployee(employeeRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PageResponseDTO<EmployeeResponseDTO> getAllEmployees(
           @PageableDefault(
                   page = 0,
                   size = 10,
                   sort = "id",
                   direction = Sort.Direction.ASC
           )
           Pageable pageable
    ) {
        return employeeService.getAllEmployees(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/fix-balances")
    public String fixOldEmployeesLeaveBalances(){
        return employeeService.fixLegacyLeaveBalances();

    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeByID(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.updateEmployee(id, employeeRequestDTO);
    }

    @PutMapping("/{id}/documents")
    public EmployeeResponseDTO uploadDocument(@PathVariable Long id, @RequestParam String documentType, @RequestParam String documentUrl) {

        return employeeService.addDocument(id, documentType, documentUrl);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }


}

