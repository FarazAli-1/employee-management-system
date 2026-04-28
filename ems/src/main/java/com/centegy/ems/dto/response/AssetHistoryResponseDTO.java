package com.centegy.ems.dto.response;


import com.centegy.ems.dto.request.EmployeeRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssetHistoryResponseDTO {

    private AssetResponseDTO asset;

    private EmployeeResponseDTO employee;

    private EmployeeResponseDTO approver;

    private LocalDate approvalDate;

    private LocalDate returnDate;


}
