package com.centegy.ems.controller;

import com.centegy.ems.dto.request.RoleRequestDTO;
import com.centegy.ems.dto.response.RoleResponseDTO;
import com.centegy.ems.model.Role;
import com.centegy.ems.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public RoleResponseDTO createRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return roleService.createRole(roleRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<RoleResponseDTO> getAllRoles()
    {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public RoleResponseDTO getRoleById(@PathVariable Long id) {
        return roleService.getRoleByID(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

}
