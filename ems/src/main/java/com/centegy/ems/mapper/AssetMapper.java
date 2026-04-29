package com.centegy.ems.mapper;

import com.centegy.ems.dto.request.AssetRequestDTO;
import com.centegy.ems.dto.response.AssetResponseDTO;
import com.centegy.ems.model.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {

    EmployeeMapper employeeMapper;

    public AssetMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public AssetResponseDTO toAssetResponseDTO(Asset asset) {

        if (asset == null) return null;

        AssetResponseDTO assetResponseDTO = new AssetResponseDTO();

        assetResponseDTO.setAssetId(asset.getId());
        assetResponseDTO.setAssetName(asset.getAssetName());
        assetResponseDTO.setAssetType(asset.getAssetType());

        if(asset.getEmployee()!=null){
            assetResponseDTO.setEmployeeId(asset.getEmployee().getId());
        }

        assetResponseDTO.setAssetStatus(asset.getStatus());

        if (asset.getApprovedBy() != null) {
            assetResponseDTO.setApproverId(asset.getApprovedBy().getId());
        }

        return assetResponseDTO;

    }

    public Asset toEntity(AssetRequestDTO assetRequestDTO) {

        if (assetRequestDTO == null) return null;

        Asset asset = new Asset();

        asset.setAssetName(assetRequestDTO.getAssetName());
        asset.setAssetType(assetRequestDTO.getAssetType());

        return  asset;

    }


}
