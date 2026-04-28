package com.centegy.ems.mapper;

import com.centegy.ems.dto.request.LeaveRequestDTO;
import com.centegy.ems.dto.response.LeaveResponseDTO;
import com.centegy.ems.model.Leave;
import org.springframework.stereotype.Component;


@Component
public class LeaveMapper {


    EmployeeMapper employeeMapper;


    public LeaveMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public LeaveResponseDTO toLeaveResponseDTO(Leave leave) {

        if (leave == null) return null;

        LeaveResponseDTO leaveResponseDTO = new LeaveResponseDTO();

        leaveResponseDTO.setLeaveType(leave.getLeaveType());
        leaveResponseDTO.setLeaveStatus(leave.getStatus());
        leaveResponseDTO.setStartDate(leave.getStartDate());
        leaveResponseDTO.setEndDate(leave.getEndDate());
        leaveResponseDTO.setEmployee(employeeMapper.toEmployeeResponseDTO(leave.getEmployee()));

        if (leave.getApprovedBy() != null) {
            leaveResponseDTO.setApproverId(leave.getApprovedBy().getId());
        }


        return leaveResponseDTO;
    }


    public Leave toEntity(LeaveRequestDTO leaveRequestDTO) {

        if (leaveRequestDTO == null) return null;


        Leave leave = new Leave();

        leave.setLeaveType(leaveRequestDTO.getLeaveType());
        leave.setStartDate(leaveRequestDTO.getStartDate());
        leave.setEndDate(leaveRequestDTO.getEndDate());


        return leave;

    }

}
