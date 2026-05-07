package com.centegy.ems.security;


import com.centegy.ems.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class JwtService {


    @Value("${jwt.secretKey}")
    private String secret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(Employee employee){

        return Jwts.builder()
                .subject(employee.getEmail())
                .claim("id", employee.getId())
                .claim("role", employee.getRole().getTitle())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getSecretKey())
                .compact();

    }

    public String getUsername(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();

    }


    private Date getExpirationDate(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();

        return claims.getExpiration();

    }

    private boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }

    public boolean isValid(String token, CustomUserDetails user){

        String userName = getUsername(token);
        return (userName.equals(user.getUsername()) && !isTokenExpired(token));



    }



}
