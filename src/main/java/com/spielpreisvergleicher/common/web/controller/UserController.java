package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.service.UserService;
import com.spielpreisvergleicher.common.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserResponse> getLoggedInUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        log.info("Received Request to get logged in User");

        String token = authHeader.substring(7);
        User user  = userService.getLoggedInUser(token);

        return ResponseEntity.ok(
                new UserResponse(user.getEmail(), user.getNickname())
        );
    }

}
