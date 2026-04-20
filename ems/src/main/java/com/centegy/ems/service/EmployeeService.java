package com.centegy.ems.service;

import com.centegy.ems.model.Employee;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.model.enums.LeaveType;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.LeaveBalanceRepository;
import com.centegy.ems.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public EmployeeService(EmployeeRepository employeeRepository, LeaveRepository leaveRepository, LeaveBalanceRepository leaveBalanceRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }


    public Employee addEmployee(Employee employee) {

        Employee newEmployee = employeeRepository.save(employee);

        // Annual Leave
        LeaveBalance annual = new LeaveBalance();
        annual.setEmployee(newEmployee);
        annual.setLeaveType(LeaveType.ANNUAL);
        annual.setRemainingDays(20);
        leaveBalanceRepository.save(annual);

        // Sick Leave
        LeaveBalance sick = new LeaveBalance();
        sick.setEmployee(newEmployee);
        sick.setLeaveType(LeaveType.SICK);
        sick.setRemainingDays(10);
        leaveBalanceRepository.save(sick);

        // Casual Leave
        LeaveBalance casual = new LeaveBalance();
        casual.setEmployee(newEmployee);
        casual.setLeaveType(LeaveType.CASUAL);
        casual.setRemainingDays(5);
        leaveBalanceRepository.save(casual);

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

        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Employee not found with ID: " + id);
        }
    }

    public Employee updateEmployee(Integer id, Employee updatedData) {

        Employee employee = getEmployeeByID(id);

        employee.setFirstName(updatedData.getFirstName());
        employee.setLastName(updatedData.getLastName());
        employee.setEmail(updatedData.getEmail());
        employee.setPhone(updatedData.getPhone());
        employee.setRole(updatedData.getRole());

        return employeeRepository.save(employee);
    }

    public String fixLegacyLeaveBalances() {
        List<Employee> employees = getAllEmployees();

        for (Employee employee : employees) {
            if (leaveBalanceRepository.findByEmployeeIdAndLeaveType(employee.getId(), LeaveType.ANNUAL).isEmpty()) {

                LeaveBalance annual = new LeaveBalance();
                annual.setEmployee(employee);
                annual.setLeaveType(LeaveType.ANNUAL);
                annual.setRemainingDays(20);
                leaveBalanceRepository.save(annual);

                // Sick Leave
                LeaveBalance sick = new LeaveBalance();
                sick.setEmployee(employee);
                sick.setLeaveType(LeaveType.SICK);
                sick.setRemainingDays(10);
                leaveBalanceRepository.save(sick);

                // Casual Leave
                LeaveBalance casual = new LeaveBalance();
                casual.setEmployee(employee);
                casual.setLeaveType(LeaveType.CASUAL);
                casual.setRemainingDays(5);
                leaveBalanceRepository.save(casual);

            }
        }
        return "All legacy employees have been fixed and given all 3 leave types";
    }

    public Employee addDocument(Integer id,String documentType , String documentUrl) {
        Employee employee = getEmployeeByID(id);

        try {
            new java.net.URL(documentUrl);
        } catch (java.net.MalformedURLException e) {
            throw new RuntimeException("Invalid document URL format! Please provide a valid web address.");
        }

        switch (documentType.toUpperCase()) {
            case "CNIC":
                employee.setCnicURL(documentUrl);
                break;
            case "CONTRACT":
                employee.setContractURL(documentUrl);
                break;
            case "CERTIFICATE":
                employee.setCertificateURL(documentUrl);
                break;
            default:
                throw new RuntimeException("Invalid document type! Use CNIC, CONTRACT, or CERTIFICATE.");

        }

       return employeeRepository.save(employee);

    }


}
