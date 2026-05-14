package com.centegy.ems.security;

import com.centegy.ems.model.Employee;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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
        return Collections.emptyList();
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
    @Override
    public boolean isEnabled() {
        return employee.isActive();
    }


}
