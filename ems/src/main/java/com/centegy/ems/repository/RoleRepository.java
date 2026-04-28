package com.centegy.ems.repository;


import com.centegy.ems.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @NonNull
    @EntityGraph(attributePaths = {"department"})
    List<Role> findAll();

}
