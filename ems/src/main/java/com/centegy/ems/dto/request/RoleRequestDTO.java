package com.centegy.ems.dto.request;

import com.centegy.ems.model.Department;
import com.centegy.ems.model.enums.RoleRank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    private Long departmentId;

    @NotNull
    private RoleRank roleRank;
}
