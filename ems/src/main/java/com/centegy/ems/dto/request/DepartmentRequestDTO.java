package com.centegy.ems.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDTO {

    @NotBlank
    private  String name;

    private  Integer headOfDepartmentId;

}
