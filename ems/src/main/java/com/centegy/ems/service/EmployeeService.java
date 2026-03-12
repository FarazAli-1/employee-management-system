package com.centegy.ems.service;

import com.centegy.ems.model.Employee;
import com.centegy.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
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
