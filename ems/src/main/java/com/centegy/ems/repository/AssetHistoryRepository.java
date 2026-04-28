package com.centegy.ems.repository;


import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.model.enums.AssetStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"asset", "employee", "approvedBy"})
    List<AssetHistory> findByAssetId(Long assetId);

    @NonNull
    @EntityGraph(attributePaths = {"asset", "employee", "approvedBy"})
    Optional<AssetHistory> findByAssetIdAndReturnDateIsNull(Long assetId);


}
