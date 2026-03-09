package com.centegy.ems.model;

import jakarta.validation.constraints.NotBlank;

public class Role {



    private Integer id;
    @NotBlank (message = "Title is required")
    private String title;
    @NotBlank (message = "Department Name is required")
    private String departmentName;

    public Role(){}


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
