package com.centegy.ems.dto.response;

import com.centegy.ems.model.enums.LeaveStatus;
import com.centegy.ems.model.enums.LeaveType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class LeaveResponseDTO {

    private Long id;

    private LeaveType leaveType;

    private LeaveStatus leaveStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long employeeId;
    private Long approverId;



}
