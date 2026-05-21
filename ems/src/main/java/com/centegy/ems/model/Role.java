package com.centegy.ems.model;

import com.centegy.ems.model.enums.RoleRank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE role SET is_active = false WHERE id=?")
//@SQLRestriction("is_active = true")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank (message = "Title is required")
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private RoleRank roleLevel;



}
