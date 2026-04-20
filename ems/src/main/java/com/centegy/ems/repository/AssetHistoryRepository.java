package com.centegy.ems.repository;


import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.model.enums.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    List<AssetHistory> findByAssetId(Long assetId);

    Optional<AssetHistory> findByAssetIdAndReturnDateIsNull(Long assetId);


}
