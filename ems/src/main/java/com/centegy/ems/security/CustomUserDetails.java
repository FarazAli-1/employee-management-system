package com.centegy.ems.security;

import com.centegy.ems.model.Employee;
import com.centegy.ems.model.enums.RoleRank;
import jakarta.persistence.*;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public CustomUserDetails(Employee employee) {
        this.employee = employee;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String roleName = employee.getRole().getRoleLevel().name();

        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    @NonNull
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return employee.isActive();
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//            return true;
//    }
//

}
