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
    public Leave getLeaveById(@PathVariable Integer id) {
        return leaveService.getLeaveById(id);
    }

    @GetMapping("/employee/{EId}")
    public List<Leave> getLeaveByEmployeeId(@PathVariable Integer EId) {
        return leaveService.getAllLeavesByEmployeeId(EId);
    }

    @PutMapping("/{id}")
    public Leave updateLeave(@PathVariable Integer id, @Valid @RequestBody Leave leave) {
        return leaveService.updateLeave(id, leave);
    }

    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
    }

    @PutMapping("/{id}/approve")
    public Leave approveLeave(@PathVariable Integer id, @RequestParam Integer managerId) {
        return leaveService.approveLeave(id, managerId);
    }


}
