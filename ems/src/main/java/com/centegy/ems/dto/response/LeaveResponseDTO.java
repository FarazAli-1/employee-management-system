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

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LeaveStatus leaveStatus;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @NotNull
    private EmployeeResponseDTO employee;

    private Long approverId;



}
