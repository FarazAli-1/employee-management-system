package com.centegy.ems.controller;

import com.centegy.ems.dto.request.AssetRequestDTO;
import com.centegy.ems.dto.response.AssetHistoryResponseDTO;
import com.centegy.ems.dto.response.AssetResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.model.Asset;
import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AssetResponseDTO createAsset(@Valid @RequestBody AssetRequestDTO assetRequestDTO) {
        return assetService.createAsset(assetRequestDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public PageResponseDTO<AssetResponseDTO> getAllAssets(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return assetService.getAllAssets(pageable);
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
    public AssetResponseDTO updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDTO assetRequestDTO) {
        return assetService.updateAsset(id, assetRequestDTO);
    }

    @PutMapping("/{id}/return")
    public AssetResponseDTO returnAsset(@PathVariable Long id) {
        return assetService.returnAsset(id);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AssetResponseDTO approveAsset(@PathVariable Long id) {
        return assetService.approveAsset(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}
