package com.centegy.ems.controller;


import com.centegy.ems.dto.request.LeaveRequestDTO;
import com.centegy.ems.dto.response.LeaveResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Leave;
import com.centegy.ems.service.LeaveService;
import jakarta.validation.Valid;
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

    @GetMapping
    public PageResponseDTO<LeaveResponseDTO> getAllLeaves(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false)int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir
    ) {
        return leaveService.getAllLeaves(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public LeaveResponseDTO getLeaveById(@PathVariable Long id) {
        return leaveService.getLeaveById(id);
    }

    @GetMapping("/calender/{requesterId}")
    public List<LeaveResponseDTO> getTeamCalender(@PathVariable Long requesterId) {
        return leaveService.leaveCalender(requesterId);
    }

   
    @GetMapping("/employee/{EId}")
    public List<LeaveResponseDTO> getLeaveByEmployeeId(@PathVariable Long EId) {
        return leaveService.getAllLeavesByEmployeeId(EId);
    }

    @PutMapping("/{id}")
    public LeaveResponseDTO updateLeave(@PathVariable Long id, @Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        return leaveService.updateLeave(id, leaveRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
    }

    @PutMapping("/{id}/approve")
    public LeaveResponseDTO approveLeave(@PathVariable Long id, @RequestParam Long managerId) {
        return leaveService.approveLeave(id, managerId);
    }



}
