package com.centegy.ems.service;


import com.centegy.ems.model.Employee;
import com.centegy.ems.model.Leave;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.model.enums.LeaveStatus;
import com.centegy.ems.model.enums.LeaveType;
import com.centegy.ems.repository.LeaveBalanceRepository;
import com.centegy.ems.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;
    private final LeaveBalanceRepository leaveBalanceRepository;


    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService, LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }


    public Leave applyForLeave(Leave leave) {

        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            throw new RuntimeException("Invalid Dates: End date cannot be before the start date!");
        }

        long daysRequested = calculateDays(leave.getStartDate(),leave.getEndDate());

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(leave.getEmployee().getId(), leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found for this employee!"));


        if (balance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient Leave Balance! You requested " + daysRequested +
                    " days but only have " + balance.getRemainingDays() + " remaining.");
        }

        if(leave.getStatus() == null) {
            leave.setStatus(LeaveStatus.PENDING);
        }
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Leave getLeaveById(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + id));
    }

    public Leave updateLeave(Long id, Leave updatedData) {
        Leave existing = getLeaveById(id);


        existing.setLeaveType(updatedData.getLeaveType());
        existing.setStartDate(updatedData.getStartDate());
        existing.setEndDate(updatedData.getEndDate());
        existing.setStatus(updatedData.getStatus());
        existing.setApprovedBy(updatedData.getApprovedBy());

        return leaveRepository.save(existing);
    }

    public void deleteLeave(Long id) {

        if (!leaveRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Leave record not found with ID: " + id);
        }
    }

    public Leave approveLeave(Long id, Long managerId) {
        Leave leave = getLeaveById(id);
        Employee requester = employeeService.getEmployeeByID(leave.getEmployee().getId());

        if (requester.getManager() == null || !requester.getManager().getId().equals(managerId)) {
            throw new RuntimeException("Unauthorized: Only the employee's assigned manager can approve this leave.");
        }

        long daysRequested = calculateDays(leave.getStartDate(), leave.getEndDate());
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(requester.getId(),  leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found!"));

        balance.setRemainingDays(balance.getRemainingDays()- (int) daysRequested);


        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(requester.getManager());
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeavesByEmployeeId(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    public List<Leave> leaveCalender(Long requesterId) {

        Employee requester = employeeService.getEmployeeByID(requesterId);
        Long teamBossId;

        if (requester.getManager() != null) {
            teamBossId = requester.getManager().getId();
        } else {
            teamBossId = requester.getId();
        }

        List<Long> teamIds = new ArrayList<>();
        teamIds.add(teamBossId);
        List<Employee> allEmployees =  employeeService.getAllEmployees();

        for (Employee emp : allEmployees) {
            if(emp.getManager() != null && emp.getManager().getId().equals(teamBossId)) {
                teamIds.add(emp.getId());
            }
        }

        List<Leave> leaves = leaveRepository.findAll();

        List<Leave> leaveList = new ArrayList<>();

        for (Leave leave : leaves) {
            if(LeaveStatus.APPROVED.equals(leave.getStatus()) && teamIds.contains(leave.getEmployee().getId())) {
                leaveList.add(leave);
            }
        }

        return leaveList;
    }

    private long calculateDays(LocalDate start, LocalDate end) {
        return end.toEpochDay() - start.toEpochDay() + 1;
    }

}
