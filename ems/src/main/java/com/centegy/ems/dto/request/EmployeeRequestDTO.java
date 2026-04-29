package com.centegy.ems.dto.request;

import com.centegy.ems.model.enums.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    private String phone;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    private Long roleId;

    private Long managerId;

}
