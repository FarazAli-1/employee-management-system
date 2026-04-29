package com.centegy.ems.dto.response;


import com.centegy.ems.model.enums.AssetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResponseDTO {

    private Long assetId;
    private String assetName;
    private String assetType;

    private AssetStatus assetStatus;

    private Long employeeId;
    private Long approverId;


}
