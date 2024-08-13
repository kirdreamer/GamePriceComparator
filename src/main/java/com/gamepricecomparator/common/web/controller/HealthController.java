package com.gamepricecomparator.common.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> getGameList() {
        log.info("Received Request to check the health");

        return ResponseEntity.ok("Ok");
    }
}
