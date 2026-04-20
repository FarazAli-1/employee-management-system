package com.centegy.ems.controller;

import com.centegy.ems.model.Asset;
import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public Asset createAsset(@Valid @RequestBody Asset asset) {
        return assetService.createAsset(asset);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{id}")
    public Asset getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }

    @GetMapping("/{id}/history")
    public List<AssetHistory> getAssetHistory(@PathVariable Long id) {
        return assetService.getAssetHistory(id);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id, @Valid @RequestBody Asset asset, @RequestParam Long performerID) {
        return assetService.updateAsset(id, asset,  performerID);
    }

    @PutMapping("/{id}/return")
    public Asset returnAsset(@PathVariable Long id, @Valid @RequestParam Long employeeId) {
        return assetService.returnAsset(id, employeeId);
    }

    @PutMapping("/{id}/approve")
    public Asset approveAsset(@PathVariable Long id, @Valid @RequestParam Long approverId) {
        return assetService.approveAsset(id, approverId);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id, @RequestParam Long performerId) {
        assetService.deleteAsset(id, performerId);
    }
}