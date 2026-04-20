package com.centegy.ems.model;

import com.centegy.ems.model.enums.LeaveStatus;
import com.centegy.ems.model.enums.LeaveType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private Employee approvedBy; // The Manager's Employee ID


}