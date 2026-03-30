package com.centegy.ems.model.enums;

public enum RoleRank {
    ADMIN(1),
    MANAGER(2),
    EMPLOYEE(3);

    private final int level;

    RoleRank(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}