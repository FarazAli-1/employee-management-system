package com.centegy.ems.mapper;


import com.centegy.ems.dto.request.AttendanceRequestDTO;
import com.centegy.ems.dto.response.AttendanceResponseDTO;
import com.centegy.ems.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    EmployeeMapper employeeMapper;

    public AttendanceMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public AttendanceResponseDTO toAttendanceResponseDTO(Attendance attendance) {

        if (attendance == null) return null;

        AttendanceResponseDTO attendanceResponseDTO = new AttendanceResponseDTO();

        attendanceResponseDTO.setAttendanceDate(attendance.getAttendanceDate());
        attendanceResponseDTO.setCheckInTime(attendance.getCheckInTime());
        attendanceResponseDTO.setCheckOutTime(attendance.getCheckOutTime());
        attendanceResponseDTO.setStatus(attendance.getStatus());
        attendanceResponseDTO.setEmployee(employeeMapper.toEmployeeResponseDTO(attendance.getEmployee()));

        return attendanceResponseDTO;
    }

    public Attendance toEntity(AttendanceRequestDTO attendanceRequestDTO) {

        if (attendanceRequestDTO == null) return null;

        Attendance attendance = new Attendance();

        attendance.setAttendanceDate(attendanceRequestDTO.getAttendanceDate());
        attendance.setCheckInTime(attendanceRequestDTO.getCheckInTime());
        attendance.setCheckOutTime(attendanceRequestDTO.getCheckOutTime());

        return attendance;

    }


}
