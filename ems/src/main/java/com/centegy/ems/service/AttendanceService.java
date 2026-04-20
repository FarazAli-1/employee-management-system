package com.centegy.ems.service;

import com.centegy.ems.model.Attendance;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.enums.AttendanceStatus;
import com.centegy.ems.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;

    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeService employeeService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }

    public Attendance checkIn(Attendance attendance) {

        Employee realEmployee = employeeService.getEmployeeByID(attendance.getEmployee().getId());

        attendance.setEmployee(realEmployee);

        attendance.setAttendanceDate(LocalDate.now());

        LocalTime startOfDay = LocalTime.of(9, 0);
        LocalTime checkInTime = attendance.getCheckInTime();

        if (checkInTime.isAfter(startOfDay)) {
            attendance.setStatus(AttendanceStatus.LATE);
        } else {
            attendance.setStatus(AttendanceStatus.ON_TIME);
        }
        return attendanceRepository.save(attendance);
    }



    public Attendance checkOut(Long attendanceId, LocalTime checkOutTime) {



        Attendance attendance =  attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found!"));
        LocalTime endOfDay = LocalTime.of(17, 30);

        attendance.setCheckOutTime(checkOutTime);

        if (checkOutTime.isBefore(endOfDay)) {
            if (AttendanceStatus.LATE.equals(attendance.getStatus())) {
                attendance.setStatus(AttendanceStatus.LATE_AND_EARLY_CHECKOUT);
            } else {
                attendance.setStatus(AttendanceStatus.EARLY_CHECKOUT);
            }
        }

        return attendanceRepository.save(attendance);
    }

    public Attendance findAttendanceById(Long id) {
        return  attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + id));
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public void deleteAttendanceById(Long id) {

        if (!attendanceRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Attendance not found with ID: " + id);
        }
    }

    public Attendance updateAttendance(Long id, Attendance updatedData) {

        Attendance attendance = findAttendanceById(id);
        attendance.setAttendanceDate(updatedData.getAttendanceDate());
        attendance.setStatus(updatedData.getStatus());
        attendance.setCheckInTime(updatedData.getCheckInTime());
        attendance.setCheckOutTime(updatedData.getCheckOutTime());
        return attendanceRepository.save(attendance);
    }
}
