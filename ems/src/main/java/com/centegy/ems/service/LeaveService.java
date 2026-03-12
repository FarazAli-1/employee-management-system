package com.centegy.ems.service;


import com.centegy.ems.model.Leave;
import com.centegy.ems.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public Leave applyForLeave(Leave leave) {
        if(leave.getStatus() == null || leave.getStatus().isBlank()) {
            leave.setStatus("PENDING");
        }
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.getAllLeaves();
    }

    public Leave getLeaveById(Integer id) {
        return leaveRepository.getLeaveById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + id));
    }

    public Leave updateLeave(Integer id, Leave updatedData) {
        Leave existing = getLeaveById(id);

        existing.setLeaveType(updatedData.getLeaveType());
        existing.setStartDate(updatedData.getStartDate());
        existing.setEndDate(updatedData.getEndDate());
        existing.setStatus(updatedData.getStatus());
        existing.setApprovedBy(updatedData.getApprovedBy());

        return leaveRepository.save(existing);
    }

    public void deleteLeave(Integer id) {
        boolean isDeleted = leaveRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Leave record not found with ID: " + id);
        }
    }

    public Leave approveLeave(Integer id, Integer managerId) {
        Leave leave = getLeaveById(id);

        leave.setStatus("APPROVED");
        leave.setApprovedBy(managerId);
        return leaveRepository.save(leave);
    }

}
