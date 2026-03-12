package com.centegy.ems.repository;

import com.centegy.ems.model.Attendance;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AttendanceRepository {

    private final List<Attendance> attendanceList = new ArrayList<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public Attendance save(Attendance attendance) {
        if(attendance.getId() == null) {
            attendance.setId(counter.incrementAndGet());
            attendanceList.add(attendance);
        }else {
            int index = -1;
            for (int i = 0; i < attendanceList.size(); i++) {
                if (attendanceList.get(i).getId().equals(attendance.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                attendanceList.set(index, attendance);
            }
        }
        return attendance;
    }

    public List<Attendance> findAll() {
        return attendanceList;
    }

    public Optional<Attendance> findById (Integer id) {
        for (Attendance attendance : attendanceList) {
            if (attendance.getId().equals(id)) {
                return Optional.of(attendance);
            }
        }
        return Optional.empty();

    }

    public boolean deleteById (Integer id) {
       return attendanceList.removeIf(attendance -> attendance.getId().equals(id));
    }

}
