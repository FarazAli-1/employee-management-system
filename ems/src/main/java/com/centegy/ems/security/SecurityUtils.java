package com.centegy.ems.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Long getCurrentUserId()  {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getEmployee().getId();

        }

        throw new RuntimeException("Unauthorized: No valid user session found");

    }



}