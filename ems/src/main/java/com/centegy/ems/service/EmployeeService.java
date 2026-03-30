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

    public String fixLegacyLeaveBalances() {
        List<Employee> employees = getAllEmployees();

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
