package com.centegy.ems.security;


import com.centegy.ems.dto.request.LoginRequestDTO;
import com.centegy.ems.dto.request.RefreshTokenRequestDTO;
import com.centegy.ems.dto.request.SignUpRequestDTO;
import com.centegy.ems.dto.response.LoginResponseDTO;
import com.centegy.ems.dto.response.SignUpResponseDTO;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.RefreshToken;
import com.centegy.ems.model.Role;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.RefreshTokenRepository;
import com.centegy.ems.repository.RoleRepository;
import com.centegy.ems.service.RefreshTokenService;
import jakarta.transaction.Transactional;
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
    private final RefreshTokenService  refreshTokenService;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, RoleRepository roleRepository, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenService = refreshTokenService;
    }


    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),loginRequestDTO.getPassword())

        );

        Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();

        String accessToken = jwtService.generateAccessToken(employee);
        String refreshToken = jwtService.generateRefreshToken(employee);

        refreshTokenService.createRefreshToken(employee.getId(), refreshToken);


        return new LoginResponseDTO(accessToken, refreshToken, employee.getId());

    }

    @Transactional
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {

        RefreshToken dbToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken());

        refreshTokenService.verifyExpiration(dbToken);

        Employee employee = dbToken.getEmployee();

        String accessToken = jwtService.generateAccessToken(employee);
        String refreshToken = jwtService.generateRefreshToken(employee);

        refreshTokenService.createRefreshToken(employee.getId(), refreshToken);

        return new LoginResponseDTO(accessToken, refreshToken,  employee.getId());

    }

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        Employee employee = new Employee();
        Role role = roleRepository.findById(signUpRequestDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        employee.setFirstName(signUpRequestDTO.getFirstName());
        employee.setLastName(signUpRequestDTO.getLastName());
        employee.setEmail(signUpRequestDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));

        employee.setRole(role);
        employee.setEmploymentType(signUpRequestDTO.getEmploymentType());

        Employee savedEmployee = employeeRepository.save(employee);
        String accessToken = jwtService.generateAccessToken(savedEmployee);
        String refreshToken = jwtService.generateRefreshToken(savedEmployee);

        refreshTokenService.createRefreshToken(savedEmployee.getId(), refreshToken);

        return new SignUpResponseDTO(accessToken, refreshToken, savedEmployee.getId());



    }



}
