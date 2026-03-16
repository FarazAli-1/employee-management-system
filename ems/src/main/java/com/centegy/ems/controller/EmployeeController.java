package com.centegy.ems.controller;


import com.centegy.ems.model.Employee;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.repository.LeaveRepository;
import com.centegy.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final LeaveRepository leaveRepository;

    public EmployeeController(EmployeeService employeeService, LeaveRepository leaveRepository) {
        this.employeeService = employeeService;
        this.leaveRepository = leaveRepository;
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
            List<Employee> employees = employeeService.getAllEmployees();

            for (Employee employee : employees) {
                if (leaveRepository.getBalanceByEmployeeId(employee.getId(), "ANNUAL").isEmpty()) {

                    // Annual Leave
                    LeaveBalance annual = new LeaveBalance();
                    annual.setEmployeeId(employee.getId());
                    annual.setLeaveType("ANNUAL");
                    annual.setRemainingDays(20);
                    leaveRepository.saveBalance(annual);

                    // Sick Leave
                    LeaveBalance sick = new LeaveBalance();
                    sick.setEmployeeId(employee.getId());
                    sick.setLeaveType("SICK");
                    sick.setRemainingDays(10);
                    leaveRepository.saveBalance(sick);

                    // Casual Leave
                    LeaveBalance casual = new LeaveBalance();
                    casual.setEmployeeId(employee.getId());
                    casual.setLeaveType("CASUAL");
                    casual.setRemainingDays(5);
                    leaveRepository.saveBalance(casual);
                }

            }

            return "All legacy employees have been fixed and given all 3 leave types";

    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeByID(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }


}

