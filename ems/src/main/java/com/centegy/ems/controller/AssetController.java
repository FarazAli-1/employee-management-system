package com.centegy.ems.controller;

import com.centegy.ems.dto.request.AssetRequestDTO;
import com.centegy.ems.dto.response.AssetHistoryResponseDTO;
import com.centegy.ems.dto.response.AssetResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
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
    public AssetResponseDTO createAsset(@Valid @RequestBody AssetRequestDTO assetRequestDTO) {
        return assetService.createAsset(assetRequestDTO);
    }

    @GetMapping
    public PageResponseDTO<AssetResponseDTO> getAllAssets(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return assetService.getAllAssets(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public AssetResponseDTO getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }

    @GetMapping("/{id}/history")
    public List<AssetHistoryResponseDTO> getAssetHistory(@PathVariable Long id) {
        return assetService.getAssetHistory(id);
    }

    @PutMapping("/{id}")
    public AssetResponseDTO updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDTO assetRequestDTO, @RequestParam Long performerID) {
        return assetService.updateAsset(id, assetRequestDTO,  performerID);
    }

    @PutMapping("/{id}/return")
    public AssetResponseDTO returnAsset(@PathVariable Long id, @RequestParam Long employeeId) {
        return assetService.returnAsset(id, employeeId);
    }

    @PutMapping("/{id}/approve")
    public AssetResponseDTO approveAsset(@PathVariable Long id, @RequestParam Long approverId) {
        return assetService.approveAsset(id, approverId);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id, @RequestParam Long performerId) {
        assetService.deleteAsset(id, performerId);
    }
}