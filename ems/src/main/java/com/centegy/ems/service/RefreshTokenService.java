package com.centegy.ems.service;

import com.centegy.ems.model.Employee;
import com.centegy.ems.model.RefreshToken;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.Date;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final EmployeeService employeeService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, EmployeeService employeeService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.employeeService = employeeService;
    }

    public RefreshToken createRefreshToken(Long id, String token) {

        refreshTokenRepository.deleteById(id);

        Employee employee = employeeService.getEmployeeEntityByID(id);
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setEmployee(employee);
        refreshToken.setExpirationDate(Instant.now().plusMillis(1000L * 60 * 60 * 24 * 7));

       return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Cannot find refresh token with token: " + token));

    }

    public void verifyExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpirationDate().compareTo(Instant.now())<0) {
            throw new RuntimeException("Refresh token has expired");
        }

    }

}
