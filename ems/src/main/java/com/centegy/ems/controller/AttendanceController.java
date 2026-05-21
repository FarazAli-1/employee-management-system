package com.centegy.ems.controller;


import com.centegy.ems.dto.request.AttendanceRequestDTO;
import com.centegy.ems.dto.response.AttendanceResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Attendance;
import com.centegy.ems.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public PageResponseDTO<AttendanceResponseDTO> getAllAttendances(
         @PageableDefault(
                 page = 0,
                 size = 10,
                 sort = "id",
                 direction = Sort.Direction.ASC
         )
         Pageable pageable
    ) {
        return attendanceService.findAll(pageable);
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
