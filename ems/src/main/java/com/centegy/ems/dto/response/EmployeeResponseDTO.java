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
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    private String email;
    private String phone;
    private EmploymentType employmentType;

    @NotNull
    private RoleResponseDTO roleId;

    private Long managerId;

}
