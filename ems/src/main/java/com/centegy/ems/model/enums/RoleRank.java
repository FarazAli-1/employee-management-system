package com.centegy.ems.model.enums;

import lombok.Getter;

@Getter
public enum RoleRank {
    ADMIN(1),
    MANAGER(2),
    EMPLOYEE(3);

    private final int level;

    RoleRank(int level) {
        this.level = level;
    }

}