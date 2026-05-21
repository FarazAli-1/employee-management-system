package com.centegy.ems.repository;

import com.centegy.ems.dto.response.EmployeeResponseDTO;
import com.centegy.ems.model.Employee;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"role", "manager"})
    Optional<Employee> findById(@NonNull Long id);

    @NonNull
    @EntityGraph(attributePaths = {"role", "manager"})
    Page<Employee> findAll(@NonNull Pageable pageable);
//
//    @NonNull
//    @Query(value = "SELECT new com.centegy.ems.dto.response.EmployeeResponseDTO" +
//            "(e.id, e.firstName,e.lastName, e.email,e.phone,e.employmentType, e.role.id, e.manager.id) " +
//            "FROM Employee e " +
//            "LEFT JOIN e.role r" +
//            "LEFT JOIN e.manager m")
//    Page<EmployeeResponseDTO> findAllAsDTOs(@NonNull Pageable pageable);


    Optional<Employee> findByEmail(String email);

}
