package com.centegy.ems.repository;

import com.centegy.ems.model.Asset;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AssetRepository {

    private final List<Asset> assetList = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Asset save(Asset asset) {
        if (asset.getId() == null) {
            asset.setId(idGenerator.getAndIncrement());
            assetList.add(asset);
        } else {
            int index = -1;
            for (int i = 0; i < assetList.size(); i++) {
                if (assetList.get(i).getId().equals(asset.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                assetList.set(index, asset);
            }
        }
        return asset;
    }

    public List<Asset> findAll() {
        return assetList;
    }

    public Optional<Asset> findById(Integer id) {
        return assetList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public boolean deleteById(Integer id) {
        return assetList.removeIf(a -> a.getId().equals(id));
    }
}