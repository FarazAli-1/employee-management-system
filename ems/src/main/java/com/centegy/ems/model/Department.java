package com.centegy.ems.model;

import jakarta.validation.constraints.NotBlank;

public class Department {

    private Integer id;

    @NotBlank(message = "Department name is required")
    private String name;
    private Integer headOfDepartmentId;

    public Department() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeadOfDepartmentId() {
        return headOfDepartmentId;
    }

    public void setHeadOfDepartmentId(Integer headOfDepartmentId) {
        this.headOfDepartmentId = headOfDepartmentId;
    }
}