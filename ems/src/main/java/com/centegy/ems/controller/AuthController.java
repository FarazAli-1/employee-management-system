package com.centegy.ems.controller;


import com.centegy.ems.dto.request.LoginRequestDTO;
import com.centegy.ems.dto.request.RefreshTokenRequestDTO;
import com.centegy.ems.dto.request.SignUpRequestDTO;
import com.centegy.ems.dto.response.LoginResponseDTO;
import com.centegy.ems.dto.response.SignUpResponseDTO;
import com.centegy.ems.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequestDTO));
    }


    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return ResponseEntity.ok(authService.signUp(signUpRequestDTO));
    }


}
