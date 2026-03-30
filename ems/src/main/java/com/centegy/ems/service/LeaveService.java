package com.centegy.ems.service;


import com.centegy.ems.model.Employee;
import com.centegy.ems.model.Leave;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;


    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
    }



    public Leave applyForLeave(Leave leave) {

        long daysRequested = calculateDays(leave.getStartDate(),leave.getEndDate());

        LeaveBalance balance = leaveRepository.getBalanceByEmployeeId(leave.getEmployeeId(), leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found for this employee!"));


        if (balance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient Leave Balance! You requested " + daysRequested +
                    " days but only have " + balance.getRemainingDays() + " remaining.");
        }

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
        Employee requester = employeeService.getEmployeeByID(leave.getEmployeeId());

        if (requester.getManagerId() == null || !requester.getManagerId().equals(managerId)) {
            throw new RuntimeException("Unauthorized: Only the employee's assigned manager can approve this leave.");
        }

        long daysRequested = calculateDays(leave.getStartDate(), leave.getEndDate());
        LeaveBalance balance = leaveRepository.getBalanceByEmployeeId(requester.getId(),  leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found!"));

        balance.setRemainingDays(balance.getRemainingDays()- (int) daysRequested);


        leave.setStatus("APPROVED");
        leave.setApprovedBy(managerId);
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeavesByEmployeeId(Integer employeeId) {
        return leaveRepository.getLeavesListByEmployee(employeeId);
    }

    public List<Leave> leaveCalender(Integer requesterId) {

        Employee requester = employeeService.getEmployeeByID(requesterId);
        int teamBossId;

        if (requester.getManagerId() != null) {
            teamBossId = requester.getManagerId();
        } else {
            teamBossId = requester.getId();
        }

        List<Integer> teamIds = new ArrayList<>();
        teamIds.add(teamBossId);
        List<Employee> allEmployees =  employeeService.getAllEmployees();

        for (Employee emp : allEmployees) {
            if(emp.getManagerId() != null && emp.getManagerId().equals(teamBossId)) {
                teamIds.add(emp.getId());
            }
        }

        List<Leave> leaves = leaveRepository.getAllLeaves();

        List<Leave> leaveList = new ArrayList<>();

        for (Leave leave : leaves) {
            if("APPROVED".equalsIgnoreCase(leave.getStatus()) && teamIds.contains(leave.getEmployeeId())) {
                leaveList.add(leave);
            }
        }

        return leaveList;
    }

    private long calculateDays(LocalDate start, LocalDate end) {
        return end.toEpochDay() - start.toEpochDay() + 1;
    }

}
