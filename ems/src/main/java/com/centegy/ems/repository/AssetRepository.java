package com.centegy.ems.repository;

import com.centegy.ems.model.Asset;
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
public interface AssetRepository extends JpaRepository<Asset, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    Page<Asset> findAll(@NonNull Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"employee", "approvedBy"})
    Optional<Asset> findById(@NonNull Long id);

}