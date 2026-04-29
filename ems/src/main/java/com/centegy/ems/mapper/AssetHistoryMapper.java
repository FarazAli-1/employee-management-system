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

        if (assetHistory.getEmployee()!=null){
            assetHistoryResponseDTO.setEmployeeId(assetHistory.getEmployee().getId());
        }
        assetHistoryResponseDTO.setApprovalDate(assetHistory.getApprovedDate());
        assetHistoryResponseDTO.setReturnDate(assetHistory.getReturnDate());

        if (assetHistory.getAsset()!=null){
            assetHistoryResponseDTO.setAssetId(assetHistory.getAsset().getId());
        }


        if (assetHistory.getApprovedBy() != null){
            assetHistoryResponseDTO.setApproverId(assetHistory.getApprovedBy().getId());
        }

        return assetHistoryResponseDTO;

    }


}
