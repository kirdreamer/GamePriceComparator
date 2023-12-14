package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;

    public boolean isLoggedIn(String token, UserDetails userDetails) {
        return jwtService.isTokenValid(token, userDetails);
    }
}
