package com.centegy.ems.service;

import com.centegy.ems.model.Attendance;
import com.centegy.ems.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance logAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance findAttendanceById(Integer id) {
        return  attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + id));
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public void deleteAttendanceById(Integer id) {
        boolean isDeleted = attendanceRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Attendance not found with ID: " + id);
        }
    }

    public Attendance updateAttendance(Integer id, Attendance updatedData) {

        Attendance attendance = findAttendanceById(id);
        attendance.setAttendanceDate(updatedData.getAttendanceDate());
        attendance.setStatus(updatedData.getStatus());
        attendance.setCheckInTime(updatedData.getCheckInTime());
        attendance.setCheckOutTime(updatedData.getCheckOutTime());
        return attendanceRepository.save(attendance);
    }
}
