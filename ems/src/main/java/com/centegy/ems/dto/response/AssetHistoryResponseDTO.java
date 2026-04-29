package com.centegy.ems.dto.response;


import com.centegy.ems.dto.request.EmployeeRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AssetHistoryResponseDTO {


    private Long assetId;

    private Long employeeId;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    private Long approverId;

    private LocalDate approvalDate;

    private LocalDate returnDate;


}
