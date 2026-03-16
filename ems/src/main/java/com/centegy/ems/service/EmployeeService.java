package com.centegy.ems.service;

import com.centegy.ems.model.Employee;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;

    public EmployeeService(EmployeeRepository employeeRepository, LeaveRepository leaveRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
    }


    public Employee addEmployee(Employee employee) {

        Employee newEmployee = employeeRepository.save(employee);

        // Annual Leave
        LeaveBalance annual = new LeaveBalance();
        annual.setEmployeeId(newEmployee.getId());
        annual.setLeaveType("ANNUAL");
        annual.setRemainingDays(20);
        leaveRepository.saveBalance(annual);

        // Sick Leave
        LeaveBalance sick = new LeaveBalance();
        sick.setEmployeeId(newEmployee.getId());
        sick.setLeaveType("SICK");
        sick.setRemainingDays(10);
        leaveRepository.saveBalance(sick);

        // Casual Leave
        LeaveBalance casual = new LeaveBalance();
        casual.setEmployeeId(newEmployee.getId());
        casual.setLeaveType("CASUAL");
        casual.setRemainingDays(5);
        leaveRepository.saveBalance(casual);

        return newEmployee;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public Employee getEmployeeByID(int id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    public void deleteEmployee(Integer id) {
        boolean isDeleted = employeeRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Employee not found with ID: " + id);
        }
    }

    public Employee updateEmployee(Integer id, Employee updatedData) {

        Employee employee = getEmployeeByID(id);

        employee.setFirstName(updatedData.getFirstName());
        employee.setLastName(updatedData.getLastName());
        employee.setEmail(updatedData.getEmail());
        employee.setPhone(updatedData.getPhone());
        employee.setRoleId(updatedData.getRoleId());

        return employeeRepository.save(employee);
    }


}
