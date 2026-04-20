package com.centegy.ems.controller;


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
    public Leave applyForLeave(@Valid @RequestBody Leave leave) {
        return leaveService.applyForLeave(leave);
    }

    @GetMapping
    public List<Leave> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @GetMapping("/{id}")
    public Leave getLeaveById(@PathVariable Long id) {
        return leaveService.getLeaveById(id);
    }

    @GetMapping("/calender/{requesterId}")
    public List<Leave> getTeamCalender(@PathVariable Long requesterId) {
        return leaveService.leaveCalender(requesterId);
    }

   
    @GetMapping("/employee/{EId}")
    public List<Leave> getLeaveByEmployeeId(@PathVariable Long EId) {
        return leaveService.getAllLeavesByEmployeeId(EId);
    }

    @PutMapping("/{id}")
    public Leave updateLeave(@PathVariable Long id, @Valid @RequestBody Leave leave) {
        return leaveService.updateLeave(id, leave);
    }

    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
    }

    @PutMapping("/{id}/approve")
    public Leave approveLeave(@PathVariable Long id, @RequestParam Long managerId) {
        return leaveService.approveLeave(id, managerId);
    }



}
