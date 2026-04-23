package com.centegy.ems.controller;

import com.centegy.ems.dto.request.RoleRequestDTO;
import com.centegy.ems.dto.response.RoleResponseDTO;
import com.centegy.ems.model.Role;
import com.centegy.ems.service.RoleService;
import jakarta.validation.Valid;
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
    public RoleResponseDTO createRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return roleService.createRole(roleRequestDTO);
    }

    @GetMapping
    public List<RoleResponseDTO> getAllRoles()
    {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public RoleResponseDTO getRoleById(@PathVariable Long id) {
        return roleService.getRoleByID(id);
    }

}
