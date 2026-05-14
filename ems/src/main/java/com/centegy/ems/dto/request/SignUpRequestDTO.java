package com.centegy.ems.dto.request;

import com.centegy.ems.model.Role;
import com.centegy.ems.model.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private EmploymentType employmentType;
    private Long roleId;




}
