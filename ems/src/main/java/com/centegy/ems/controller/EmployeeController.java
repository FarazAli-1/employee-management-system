package com.centegy.ems.controller;


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
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/admin/fix-balances")
    public String fixOldEmployeesLeaveBalances(){
        return employeeService.fixLegacyLeaveBalances();

    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeByID(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @PutMapping("/{id}/documents")
    public Employee uploadDocument(@PathVariable Integer id, @RequestParam String documentType, @RequestParam String documentUrl) {

        return employeeService.addDocument(id, documentType, documentUrl);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }


}

