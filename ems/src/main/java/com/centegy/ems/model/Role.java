package com.centegy.ems.model;

import com.centegy.ems.model.enums.RoleRank;
import jakarta.validation.constraints.NotBlank;

public class Role {



    private Integer id;
    @NotBlank (message = "Title is required")
    private String title;
    @NotBlank (message = "Department Name is required")
    private String departmentName;

    private RoleRank roleLevel;

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

    public RoleRank getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(RoleRank roleLevel) {
        this.roleLevel = roleLevel;
    }


}
