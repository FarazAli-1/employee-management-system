package com.centegy.ems.service;

import com.centegy.ems.dto.request.AttendanceRequestDTO;
import com.centegy.ems.dto.response.AttendanceResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.exceptions.ResourceNotFoundException;
import com.centegy.ems.exceptions.UnauthorizedActionException;
import com.centegy.ems.mapper.AttendanceMapper;
import com.centegy.ems.model.Attendance;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.enums.AttendanceStatus;
import com.centegy.ems.repository.AttendanceRepository;
import com.centegy.ems.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;
    private final AttendanceMapper  attendanceMapper;

    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeService employeeService, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
        this.attendanceMapper = attendanceMapper;
    }

    public AttendanceResponseDTO checkIn(AttendanceRequestDTO attendanceRequestDTO) {

        Attendance attendance = attendanceMapper.toEntity(attendanceRequestDTO);

        Employee realEmployee = employeeService.getEmployeeEntityByID(SecurityUtils.getCurrentUserId());

        attendance.setEmployee(realEmployee);

        attendance.setAttendanceDate(LocalDate.now());

        LocalTime startOfDay = LocalTime.of(9, 0);
        LocalTime checkInTime = attendance.getCheckInTime();

        if (checkInTime.isAfter(startOfDay)) {
            attendance.setStatus(AttendanceStatus.LATE);
        } else {
            attendance.setStatus(AttendanceStatus.ON_TIME);
        }

        Attendance saveAttendance = attendanceRepository.save(attendance);

        return attendanceMapper.toAttendanceResponseDTO(saveAttendance);
    }



    public AttendanceResponseDTO checkOut(Long attendanceId, LocalTime checkOutTime) {

        Attendance attendance =  attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found!"));

        if (!attendance.getEmployee().getId().equals(SecurityUtils.getCurrentUserId())) {
            throw new UnauthorizedActionException("You can only checkout your own attendance");
        }

        LocalTime endOfDay = LocalTime.of(17, 30);

        attendance.setCheckOutTime(checkOutTime);

        if (checkOutTime.isBefore(endOfDay)) {
            if (AttendanceStatus.LATE.equals(attendance.getStatus())) {
                attendance.setStatus(AttendanceStatus.LATE_AND_EARLY_CHECKOUT);
            } else {
                attendance.setStatus(AttendanceStatus.EARLY_CHECKOUT);
            }
        }

        Attendance saveAttendance = attendanceRepository.save(attendance);

        return attendanceMapper.toAttendanceResponseDTO(saveAttendance);
    }

    public Attendance findAttendanceEntityById(Long id) {

       return attendanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));

    }

    public AttendanceResponseDTO findAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));

        return  attendanceMapper.toAttendanceResponseDTO(attendance);
    }

    public PageResponseDTO<AttendanceResponseDTO> findAll(Pageable pageable) {

        Page<Attendance> attendancePage = attendanceRepository.findAll(pageable);
        List<AttendanceResponseDTO> content = attendancePage.getContent()
                .stream()
                .map(attendanceMapper::toAttendanceResponseDTO)
                .toList();

        PageResponseDTO<AttendanceResponseDTO> response = new PageResponseDTO<>();

        response.setContent(content);
        response.setPageNo(attendancePage.getNumber());
        response.setPageSize(attendancePage.getSize());
        response.setTotalPages(attendancePage.getTotalPages());
        response.setTotalElements(attendancePage.getTotalElements());
        response.setLast(attendancePage.isLast());

        return response;

    }

    public void deleteAttendanceById(Long id) {

        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Attendance not found with ID: " + id);
        }

        attendanceRepository.deleteById(id);

    }

    public AttendanceResponseDTO updateAttendance(Long id, AttendanceRequestDTO updatedData) {

        Attendance attendance = findAttendanceEntityById(id);

        attendance.setAttendanceDate(updatedData.getAttendanceDate());
        attendance.setCheckInTime(updatedData.getCheckInTime());
        attendance.setCheckOutTime(updatedData.getCheckOutTime());

        Attendance attendanceUpdated = attendanceRepository.save(attendance);

        return attendanceMapper.toAttendanceResponseDTO(attendanceUpdated);
    }
}
