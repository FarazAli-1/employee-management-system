package com.centegy.ems.dto.response;

import com.centegy.ems.model.enums.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private EmploymentType employmentType;

    private Long roleId;
    private Long managerId;

}
