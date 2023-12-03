package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.config.JwtService;
import com.spielpreisvergleicher.common.entity.user.Role;
import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.repository.UserRepository;
import com.spielpreisvergleicher.common.web.request.AuthenticationRequest;
import com.spielpreisvergleicher.common.web.request.RegisterRequest;
import com.spielpreisvergleicher.common.web.response.AuthenticationResponse;
import com.spielpreisvergleicher.common.web.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.email())
                .nickname(request.nickname())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new RegisterResponse(jwt);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }
}
