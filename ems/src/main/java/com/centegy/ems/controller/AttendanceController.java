package com.centegy.ems.controller;


import com.centegy.ems.dto.request.AttendanceRequestDTO;
import com.centegy.ems.dto.response.AttendanceResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
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
    public AttendanceResponseDTO addAttendance(@Valid @RequestBody AttendanceRequestDTO attendance) {
        return attendanceService.checkIn(attendance);
    }

    @GetMapping
    public PageResponseDTO<AttendanceResponseDTO> getAllAttendances(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return attendanceService.findAll(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public AttendanceResponseDTO getAttendanceById(@PathVariable Long id) {
        return attendanceService.findAttendanceById(id);
    }

    @PutMapping("/{id}")
    public AttendanceResponseDTO updateAttendance(@PathVariable Long id, @Valid @RequestBody AttendanceRequestDTO attendance) {
        return attendanceService.updateAttendance(id, attendance);
    }

    @PutMapping("/checkout/{id}")
    public AttendanceResponseDTO checkOut(@PathVariable Long id, @RequestParam LocalTime checkOutTime) {
        return attendanceService.checkOut(id, checkOutTime);
    }
    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendanceById(id);
    }

}
