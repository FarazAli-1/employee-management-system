package com.centegy.ems.service;


import com.centegy.ems.dto.request.LeaveRequestDTO;
import com.centegy.ems.dto.response.LeaveResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.mapper.LeaveMapper;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.Leave;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.model.enums.LeaveStatus;
import com.centegy.ems.repository.LeaveBalanceRepository;
import com.centegy.ems.repository.LeaveRepository;
import com.centegy.ems.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveMapper leaveMapper;

    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService, LeaveBalanceRepository leaveBalanceRepository, LeaveMapper leaveMapper, SecurityUtils securityUtils) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveMapper = leaveMapper;
    }


    public LeaveResponseDTO applyForLeave(LeaveRequestDTO leaveRequestDTO) {

        Leave leave = leaveMapper.toEntity(leaveRequestDTO);

        Employee employee = employeeService.getEmployeeEntityByID(SecurityUtils.getCurrentUserId());
        leave.setEmployee(employee);

        if (leaveRequestDTO.getEndDate().isBefore(leaveRequestDTO.getStartDate())) {
            throw new RuntimeException("Invalid Dates: End date cannot be before the start date!");
        }

        long daysRequested = calculateDays(leaveRequestDTO.getStartDate(),leaveRequestDTO.getEndDate());

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(employee.getId(), leaveRequestDTO.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found for this employee!"));



        if (balance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient Leave Balance! You requested " + daysRequested +
                    " days but only have " + balance.getRemainingDays() + " remaining.");
        }

        leave.setStatus(LeaveStatus.PENDING);

        return leaveMapper.toLeaveResponseDTO(leaveRepository.save(leave));

    }

    public PageResponseDTO<LeaveResponseDTO> getAllLeaves(Pageable pageable) {

        Page<Leave> leavePage = leaveRepository.findAll(pageable);
        List<LeaveResponseDTO>  content = leavePage.getContent()
                .stream()
                .map(leaveMapper::toLeaveResponseDTO)
                .toList();

        PageResponseDTO<LeaveResponseDTO> response = new PageResponseDTO<>();

        response.setContent(content);
        response.setPageSize(leavePage.getSize());
        response.setTotalPages(leavePage.getTotalPages());
        response.setTotalElements(leavePage.getTotalElements());
        response.setPageNo(leavePage.getNumber());
        response.setLast(leavePage.isLast());

        return response;

    }

    public Leave getLeaveEntityByID(Long id) {

        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + id));

    }

    public LeaveResponseDTO getLeaveById(Long id) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with ID: " + id));

        return leaveMapper.toLeaveResponseDTO(leave);
    }

    public LeaveResponseDTO updateLeave(Long id, LeaveRequestDTO updatedData) {

        Leave existing = getLeaveEntityByID(id);

        existing.setLeaveType(updatedData.getLeaveType());
        existing.setStartDate(updatedData.getStartDate());
        existing.setEndDate(updatedData.getEndDate());

        return leaveMapper.toLeaveResponseDTO(leaveRepository.save(existing));

    }

    public void deleteLeave(Long id) {

        Leave leave = getLeaveEntityByID(id);

        if (leave.getEmployee().getId().equals(SecurityUtils.getCurrentUserId()) && leave.getStatus().equals(LeaveStatus.PENDING)) {
            leaveRepository.deleteById(id);
        }

        if (!leaveRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Leave record not found with ID: " + id);
        }
    }

    public LeaveResponseDTO approveLeave(Long id) {
        Leave leave = getLeaveEntityByID(id);

        Employee requester = employeeService.getEmployeeEntityByID(leave.getEmployee().getId());
        leave.setEmployee(requester);

        if (requester.getManager() == null || !requester.getManager().getId().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Unauthorized: Only the employee's assigned manager can approve this leave.");
        }

        long daysRequested = calculateDays(leave.getStartDate(), leave.getEndDate());
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(requester.getId(),  leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave Balance not found!"));

        balance.setRemainingDays(balance.getRemainingDays()- (int) daysRequested);


        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(requester.getManager());

        return leaveMapper.toLeaveResponseDTO(leaveRepository.save(leave));
    }

    public List<LeaveResponseDTO> getAllLeavesByEmployeeId() {

        return leaveRepository.findByEmployeeId(SecurityUtils.getCurrentUserId()).stream().map(leaveMapper::toLeaveResponseDTO).toList();

    }

    public List<LeaveResponseDTO> leaveCalender() {

        Employee requester = employeeService.getEmployeeEntityByID(SecurityUtils.getCurrentUserId());
        Long teamBossId;

        if (requester.getManager() != null) {
            teamBossId = requester.getManager().getId();
        } else {
            teamBossId = requester.getId();
        }

        List<Long> teamIds = new ArrayList<>();
        teamIds.add(teamBossId);
        List<Employee> allEmployees =  employeeService.getAllEmployeesEntities();

        for (Employee emp : allEmployees) {
            if(emp.getManager() != null && emp.getManager().getId().equals(teamBossId)) {
                teamIds.add(emp.getId());
            }
        }

        List<Leave> leaves = leaveRepository.findAll();

        List<LeaveResponseDTO> leaveList = new ArrayList<>();

        for (Leave leave : leaves) {

            if(LeaveStatus.APPROVED.equals(leave.getStatus()) && teamIds.contains(leave.getEmployee().getId())) {
                leaveList.add(leaveMapper.toLeaveResponseDTO(leave));
            }
        }



        return leaveList;
    }

    private long calculateDays(LocalDate start, LocalDate end) {
        return end.toEpochDay() - start.toEpochDay() + 1;
    }

}
