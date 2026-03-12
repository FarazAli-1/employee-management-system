package com.centegy.ems.repository;


import com.centegy.ems.model.Leave;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class LeaveRepository {

    private final List<Leave> leavesList = new ArrayList<>();

    private AtomicInteger counter = new AtomicInteger(0);


    public Leave save(Leave leave) {
        if (leave.getId() == null) {
            leave.setId(counter.incrementAndGet());
            leavesList.add(leave);

        }else {
            int index = -1;
            for (int i = 0; i < leavesList.size(); i++) {
                if (leavesList.get(i).getId().equals(leave.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                leavesList.set(index, leave);
            }
        }
        return leave;
    }

    public List<Leave> getAllLeaves(){
        return leavesList;
    }

    public Optional<Leave> getLeaveById(int id) {
        for (Leave leave : leavesList) {
            if (leave.getId() == id) {
                return Optional.of(leave);
            }
        }
        return Optional.empty();
    }

    public boolean deleteById(Integer id) {
        return leavesList.removeIf(leave -> leave.getId().equals(id));
    }

}
