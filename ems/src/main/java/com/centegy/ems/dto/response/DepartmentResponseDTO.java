package com.centegy.ems.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DepartmentResponseDTO implements Serializable {

    private Long id;
    private String name;
    private Integer headOfDepartmentId;

}
