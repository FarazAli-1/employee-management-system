package com.centegy.ems.model;

import com.centegy.ems.model.enums.AssetStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Asset Name is required")
    private String assetName;

    @NotBlank
    private String assetType;

    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private Employee approvedBy;


}