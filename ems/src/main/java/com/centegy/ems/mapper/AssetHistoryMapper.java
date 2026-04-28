package com.centegy.ems.mapper;


import com.centegy.ems.dto.response.AssetHistoryResponseDTO;
import com.centegy.ems.model.AssetHistory;
import org.springframework.stereotype.Component;

@Component
public class AssetHistoryMapper {

    EmployeeMapper employeeMapper;
    AssetMapper assetMapper;

    public AssetHistoryMapper(EmployeeMapper employeeMapper, AssetMapper assetMapper) {
        this.employeeMapper = employeeMapper;
        this.assetMapper = assetMapper;
    }

    public AssetHistoryResponseDTO toAssetHistoryResponseDTO(AssetHistory assetHistory) {

        if (assetHistory == null) return null;

        AssetHistoryResponseDTO assetHistoryResponseDTO = new AssetHistoryResponseDTO();

        assetHistoryResponseDTO.setEmployee(employeeMapper.toEmployeeResponseDTO(assetHistory.getEmployee()));
        assetHistoryResponseDTO.setApprovalDate(assetHistory.getApprovedDate());
        assetHistoryResponseDTO.setReturnDate(assetHistory.getReturnDate());
        assetHistoryResponseDTO.setAsset(assetMapper.toAssetResponseDTO(assetHistory.getAsset()));

        if (assetHistory.getApprovedBy() != null){
            assetHistoryResponseDTO.setApprover(employeeMapper.toEmployeeResponseDTO(assetHistory.getApprovedBy()));
        }

        return assetHistoryResponseDTO;

    }


}
