package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.dto.LoginResultDTO;
import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.service.AuthenticationService;
import com.gamepricecomparator.common.web.request.AuthenticationRequest;
import com.gamepricecomparator.common.web.request.RegisterRequest;
import com.gamepricecomparator.common.web.response.AuthenticationResponse;
import com.gamepricecomparator.common.web.response.IsLoggedInResponse;
import com.gamepricecomparator.common.web.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok(new RegisterResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Received Request to authenticate a User");
        LoginResultDTO result = authenticationService.authenticate(request.email(), request.password());

        return ResponseEntity.ok(new AuthenticationResponse(result.token(), result.nickname()));
    }

    @GetMapping("/user")
    public ResponseEntity<IsLoggedInResponse> getLoggedInUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Authentication authentication
    ) {
        log.info("Received Request to get logged in User");

        log.info(authentication.toString());
        User user = (User) authentication.getPrincipal();
        String token = authHeader.substring(7);

        return ResponseEntity.ok(
                new IsLoggedInResponse(user.getEmail(), user.getNickname(), token, authenticationService.isLoggedIn(token, user))
        );
    }
}
