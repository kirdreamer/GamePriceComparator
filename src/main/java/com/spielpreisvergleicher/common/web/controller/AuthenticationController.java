package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.dto.LoginResult;
import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.service.AuthenticationService;
import com.spielpreisvergleicher.common.web.request.AuthenticationRequest;
import com.spielpreisvergleicher.common.web.request.RegisterRequest;
import com.spielpreisvergleicher.common.web.response.AuthenticationResponse;
import com.spielpreisvergleicher.common.web.response.IsLoggedInResponse;
import com.spielpreisvergleicher.common.web.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        log.info("Received Request to register new User");
        String token = authenticationService.register(request.email(), request.nickname(), request.password());

        return ResponseEntity.ok((new RegisterResponse(token)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Received Request to authenticate a User");
        LoginResult result = authenticationService.authenticate(request.email(), request.password());

        return ResponseEntity.ok(new AuthenticationResponse(result.token(), result.nickname()));
    }

    @GetMapping("/user")
    public ResponseEntity<IsLoggedInResponse> getLoggedInUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Authentication authentication
    ) {
        log.info("Received Request to get logged in User");

        User user = (User) authentication.getPrincipal();
        String token = authHeader.substring(7);

        return ResponseEntity.ok(
                new IsLoggedInResponse(user.getEmail(), user.getNickname(), authenticationService.isLoggedIn(token, user))
        );
    }
}
