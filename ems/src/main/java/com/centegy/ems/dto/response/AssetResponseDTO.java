package com.centegy.ems.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResponseDTO {

    private Long assetId;

    private String assetName;

    private String assetType;

    private EmployeeResponseDTO employee;

    private Long approverId;


}
