package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.web.request.AuthenticationRequest;
import com.spielpreisvergleicher.common.web.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request
    ) {
        log.info("Received Request to register new User");
        return null;
    }

    @PostMapping("/authenticate")
    public String register(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Received Request to authenticate a User");
        return null;
    }
}
