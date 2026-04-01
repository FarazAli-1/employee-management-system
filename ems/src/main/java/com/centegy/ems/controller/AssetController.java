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
    public Asset getAssetById(@PathVariable Integer id) {
        return assetService.getAssetById(id);
    }

    @GetMapping("/{id}/history")
    public List<AssetHistory> getAssetHistory(@PathVariable Integer id) {
        return assetService.getAssetHistory(id);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Integer id, @Valid @RequestBody Asset asset, @RequestParam Integer performerID) {
        return assetService.updateAsset(id, asset,  performerID);
    }

    @PutMapping("/{id}/return")
    public Asset returnAsset(@PathVariable Integer id, @Valid @RequestParam Integer employeeId) {
        return assetService.returnAsset(id, employeeId);
    }

    @PutMapping("/{id}/approve")
    public Asset approveAsset(@PathVariable Integer id, @Valid @RequestParam Integer approverId) {
        return assetService.approveAsset(id, approverId);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Integer id, @RequestParam Integer performerId) {
        assetService.deleteAsset(id, performerId);
    }
}