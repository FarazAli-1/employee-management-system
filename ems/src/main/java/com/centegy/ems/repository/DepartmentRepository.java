package com.centegy.ems.repository;

import com.centegy.ems.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class DepartmentRepository {

    private final List<Department> departmentList = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Department save(Department department) {
        if (department.getId() == null) {
            department.setId(idGenerator.getAndIncrement());
            departmentList.add(department);
        } else {
            int index = -1;
            for (int i = 0; i < departmentList.size(); i++) {
                if (departmentList.get(i).getId().equals(department.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                departmentList.set(index, department);
            }
        }
        return department;
    }

    public List<Department> findAll() {
        return departmentList;
    }

    public Optional<Department> findById(Integer id) {
        return departmentList.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    public boolean deleteById(Integer id) {
        return departmentList.removeIf(d -> d.getId().equals(id));
    }
}