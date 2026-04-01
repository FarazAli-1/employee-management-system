package com.centegy.ems.model;

import com.centegy.ems.model.enums.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Employee {

    private Integer id;

    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    private String phone;

    @NotNull
    private EmploymentType employmentType;

    private String cnicURL;
    private String contractURL;
    private String certificateURL;

    @NotNull(message = "Every employee must be assigned to a Role ID")
    private Integer roleId;
    private Integer managerId;

    private List<LeaveBalance> leaveBalances = new ArrayList<>();

    public Employee(){}


    public String getCertificateURL() {
        return certificateURL;
    }

    public void setCertificateURL(String certificateURL) {
        this.certificateURL = certificateURL;
    }

    public String getCnicURL() {
        return cnicURL;
    }

    public void setCnicURL(String cnicURL) {
        this.cnicURL = cnicURL;
    }

    public String getContractURL() {
        return contractURL;
    }

    public void setContractURL(String contractURL) {
        this.contractURL = contractURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<LeaveBalance> getLeaveBalances() {
        return leaveBalances;
    }

    public void setLeaveBalances(List<LeaveBalance> leaveBalances) {
        this.leaveBalances = leaveBalances;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }



}
