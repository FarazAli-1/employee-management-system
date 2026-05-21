package com.centegy.ems.controller;


import com.centegy.ems.dto.request.LeaveRequestDTO;
import com.centegy.ems.dto.response.LeaveResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Leave;
import com.centegy.ems.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public LeaveResponseDTO applyForLeave(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        return leaveService.applyForLeave(leaveRequestDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public PageResponseDTO<LeaveResponseDTO> getAllLeaves(
        @PageableDefault(
                page = 0,
                size = 10,
                sort = "id",
                direction = Sort.Direction.ASC
        )
        Pageable  pageable
    ) {
        return leaveService.getAllLeaves(pageable);
    }

    @GetMapping("/{id}")
    public LeaveResponseDTO getLeaveById(@PathVariable Long id) {
        return leaveService.getLeaveById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/calender")
    public List<LeaveResponseDTO> getTeamCalender() {
        return leaveService.leaveCalender();
    }

   
    @GetMapping("/employee")
    public List<LeaveResponseDTO> getLeaveByEmployeeId() {
        return leaveService.getAllLeavesByEmployeeId();
    }

    @PutMapping("/{id}")
    public LeaveResponseDTO updateLeave(@PathVariable Long id, @Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        return leaveService.updateLeave(id, leaveRequestDTO);
    }


    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}/approve")
    public LeaveResponseDTO approveLeave(@PathVariable Long id) {
        return leaveService.approveLeave(id);
    }



}
