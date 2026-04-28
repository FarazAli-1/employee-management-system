package com.centegy.ems.repository;


import com.centegy.ems.model.Leave;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    List<Leave> findByEmployeeId(Long employeeId);

    @NonNull
    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    List<Leave> findAll();

    @NonNull
    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    Page<Leave> findAll(@NonNull Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    Optional<Leave> findById(@NonNull Long id);

}
