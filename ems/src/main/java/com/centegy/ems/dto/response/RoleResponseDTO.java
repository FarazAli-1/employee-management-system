package com.centegy.ems.dto.response;


import com.centegy.ems.model.enums.RoleRank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDTO {

    private Long id;
    private String title;
    private DepartmentResponseDTO department;
    private RoleRank roleRank;

}
