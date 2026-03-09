package com.centegy.ems.controller;

import com.centegy.ems.model.Role;
import com.centegy.ems.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping
    public List<Role> getAllRoles()
    {
        return roleService.getAllRoles();
    }

}
