package com.centegy.ems.service;

import com.centegy.ems.model.Asset;
import com.centegy.ems.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset createAsset(Asset asset) {
        if (asset.getStatus() == null || asset.getStatus().isBlank()) {
            asset.setStatus("REQUESTED");
        }
        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Integer id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + id));
    }

    public Asset updateAsset(Integer id, Asset updatedData) {
        Asset asset = getAssetById(id);

        asset.setAssetName(updatedData.getAssetName());
        asset.setAssetType(updatedData.getAssetType());
        asset.setStatus(updatedData.getStatus());
        asset.setEmployeeId(updatedData.getEmployeeId());

        return assetRepository.save(asset);
    }

    public void deleteAsset(Integer id) {
        boolean isDeleted = assetRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Asset not found with ID: " + id);
        }
    }
}