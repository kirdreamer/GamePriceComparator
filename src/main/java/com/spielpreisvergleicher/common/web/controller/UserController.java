package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.service.UserService;
import com.spielpreisvergleicher.common.web.response.IsLoggedInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/user"})
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<IsLoggedInResponse> getLoggedInUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Authentication authentication
    ) {
        log.info("Received Request to get logged in User");

        User user = (User) authentication.getPrincipal();
        String token = authHeader.substring(7);

        return ResponseEntity.ok(
                new IsLoggedInResponse(user.getEmail(), user.getNickname(), userService.isLoggedIn(token, user))
        );
    }

}
