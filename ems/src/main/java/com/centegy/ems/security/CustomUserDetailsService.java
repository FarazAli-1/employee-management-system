package com.centegy.ems.security;

import com.centegy.ems.model.Employee;
import com.centegy.ems.repository.EmployeeRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        return new CustomUserDetails(employee);

    }
}
