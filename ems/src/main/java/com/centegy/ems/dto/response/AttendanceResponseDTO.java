package com.centegy.ems.dto.response;

import com.centegy.ems.model.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceResponseDTO {

    private LocalDate attendanceDate;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private AttendanceStatus status;

    private EmployeeResponseDTO employee;


}
