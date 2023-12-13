package com.spielpreisvergleicher.common.web.controller;

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

//    private final UserService userService;

    @GetMapping()
    public ResponseEntity<String> getLoggedInUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        log.info("Received Request to get logged in User" + authHeader);
//        String token = userService.getLoggedInUser(authHeader);

        return ResponseEntity.ok(authHeader);
    }

}
