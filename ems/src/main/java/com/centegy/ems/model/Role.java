package com.centegy.ems.model;

import com.centegy.ems.model.enums.RoleRank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Role {



    private Integer id;
    @NotBlank (message = "Title is required")
    private String title;
    @NotNull
    private Integer departmentId;

    private RoleRank roleLevel;

    public Role(){}



    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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
