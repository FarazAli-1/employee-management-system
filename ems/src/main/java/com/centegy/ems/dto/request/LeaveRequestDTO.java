package com.centegy.ems.dto.request;

import com.centegy.ems.model.enums.LeaveStatus;
import com.centegy.ems.model.enums.LeaveType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequestDTO {

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @NotNull
    private Long employeeId;

}
