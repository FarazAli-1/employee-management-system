package com.centegy.ems.repository;

import com.centegy.ems.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class EmployeeRepository {

    private final List<Employee> employeeList = new ArrayList<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public Employee save(Employee employee) {
        if(employee.getId() == null) {
            employee.setId(counter.incrementAndGet());
            employeeList.add(employee);

        }
        else {
            int index = -1;
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getId().equals(employee.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                employeeList.set(index, employee);
            }
        }

        return employee;

    }

    public Optional<Employee> findById(Integer id) {
        for (Employee employee : employeeList) {
            if (employee.getId().equals(id)) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }

    public boolean deleteById(Integer id) {
        return employeeList.removeIf(emp -> emp.getId().equals(id));
    }

    public List<Employee> findAll() {
        return employeeList;
    }

}
