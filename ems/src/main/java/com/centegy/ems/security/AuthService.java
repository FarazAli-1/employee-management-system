package com.centegy.ems.security;


import com.centegy.ems.dto.request.LoginRequestDTO;
import com.centegy.ems.dto.request.SignUpRequestDTO;
import com.centegy.ems.dto.response.LoginResponseDTO;
import com.centegy.ems.dto.response.SignUpResponseDTO;
import com.centegy.ems.model.Employee;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }


    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),loginRequestDTO.getPassword())

        );

        Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();

        String token = jwtService.generateToken(employee);

        return new LoginResponseDTO(token, employee.getId());

    }

    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        Employee employee = new Employee();
        employee.setFirstName(signUpRequestDTO.getFirstName());
        employee.setLastName(signUpRequestDTO.getLastName());
        employee.setEmail(signUpRequestDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));

        employee.setRole(roleRepository.getReferenceById(signUpRequestDTO.getRoleId()));
        employee.setEmploymentType(signUpRequestDTO.getEmploymentType());

        Employee savedEmployee = employeeRepository.save(employee);
        String token = jwtService.generateToken(savedEmployee);

        return new SignUpResponseDTO(savedEmployee.getId(), token);



    }



}
