package com.centegy.ems.model;

import com.centegy.ems.model.enums.LeaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LeaveBalance {

    @NotNull
    private LeaveType leaveType;
    @Min(0)
    private int remainingDays;
    @NotNull
    private Integer employeeId;


    public LeaveBalance(){}  //no-arg const needed for spring boot


    public LeaveBalance(LeaveType leaveType, int remainingDays) {
        this.leaveType = leaveType;
        this.remainingDays = remainingDays;
    }



    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
