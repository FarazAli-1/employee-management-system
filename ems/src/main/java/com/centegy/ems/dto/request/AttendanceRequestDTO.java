package com.centegy.ems.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceRequestDTO {

    @NotNull
    private LocalDate attendanceDate;

    @NotNull
    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    @NotNull
    private Long employeeId;



}
