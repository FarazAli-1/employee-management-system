package com.centegy.ems.repository;

import com.centegy.ems.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

}