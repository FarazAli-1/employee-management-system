package com.centegy.ems.repository;


import com.centegy.ems.model.Leave;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.model.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {


    List<Leave> findByEmployeeId(Long employeeId);


}
