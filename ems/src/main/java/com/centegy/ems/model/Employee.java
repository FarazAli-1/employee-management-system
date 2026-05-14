package com.centegy.ems.model;

import com.centegy.ems.model.enums.EmploymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE employee SET is_active = false WHERE id=?")
@SQLRestriction("is_active = true")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    private String cnicURL;
    private String contractURL;
    private String certificateURL;

    @NotNull
    private String password;

    private boolean isActive = true;

    @NotNull(message = "Every employee must be assigned to a Role ID")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;


}
