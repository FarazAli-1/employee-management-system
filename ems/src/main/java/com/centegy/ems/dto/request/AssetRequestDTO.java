package com.centegy.ems.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetRequestDTO {

    @NotBlank
    private String assetName;

    @NotBlank
    private String assetType;

}
