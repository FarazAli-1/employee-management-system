package com.centegy.ems.controller;


import com.centegy.ems.model.Attendance;
import com.centegy.ems.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public Attendance addAttendance(@Valid @RequestBody Attendance attendance) {
        return attendanceService.checkIn(attendance);
    }

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceService.findAll();
    }

    @GetMapping("/{id}")
    public Attendance getAttendanceById(@PathVariable Integer id) {
        return attendanceService.findAttendanceById(id);
    }

    @PutMapping("/{id}")
    public Attendance updateAttendance(@PathVariable Integer id, @Valid @RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(id, attendance);
    }

    @PutMapping("/checkout/{id}")
    public Attendance checkOut(@PathVariable Integer id, @RequestParam LocalTime checkOutTime) {
        return attendanceService.checkOut(id, checkOutTime);
    }
    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendanceById(id);
    }

}
