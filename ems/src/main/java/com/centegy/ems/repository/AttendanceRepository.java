package com.centegy.ems.repository;

import com.centegy.ems.model.Attendance;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    @NonNull
    @EntityGraph(attributePaths = {"employee"})
    Optional<Attendance> findById(@NonNull Long id);

    @NonNull
    @EntityGraph(attributePaths = {"employee"})
    Page<Attendance> findAll(@NonNull Pageable pageable);

}
