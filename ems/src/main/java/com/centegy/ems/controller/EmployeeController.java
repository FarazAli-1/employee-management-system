package com.centegy.ems.controller;


import com.centegy.ems.dto.request.EmployeeRequestDTO;
import com.centegy.ems.dto.response.EmployeeResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Employee;
import com.centegy.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @PostMapping
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.addEmployee(employeeRequestDTO);
    }

    @GetMapping
    public PageResponseDTO<EmployeeResponseDTO> getAllEmployees(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return employeeService.getAllEmployees(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/admin/fix-balances")
    public String fixOldEmployeesLeaveBalances(){
        return employeeService.fixLegacyLeaveBalances();

    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeByID(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.updateEmployee(id, employeeRequestDTO);
    }

    @PutMapping("/{id}/documents")
    public EmployeeResponseDTO uploadDocument(@PathVariable Long id, @RequestParam String documentType, @RequestParam String documentUrl) {

        return employeeService.addDocument(id, documentType, documentUrl);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }


}

