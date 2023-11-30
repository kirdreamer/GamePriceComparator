package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.service.AuthenticationService;
import com.spielpreisvergleicher.common.web.request.AuthenticationRequest;
import com.spielpreisvergleicher.common.web.request.RegisterRequest;
import com.spielpreisvergleicher.common.web.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        log.info("Received Request to register new User");
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Received Request to authenticate a User");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
