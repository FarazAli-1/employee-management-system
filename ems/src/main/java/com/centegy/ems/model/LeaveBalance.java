package com.centegy.ems.model;

import com.centegy.ems.model.enums.LeaveType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    @Min(0)
    private int remainingDays;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public LeaveBalance(LeaveType leaveType, int remainingDays) {
        this.leaveType = leaveType;
        this.remainingDays = remainingDays;
    }


}
