package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.FavoriteGameService;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {
    private final FavoriteGameService favoriteGameService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFavoriteGameToUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody FavoriteGameRequest favoriteGameRequest
    ) {
        log.info("Received Request to add a new Favorite Game with name {}", favoriteGameRequest.name());
        favoriteGameService.saveFavoriteGameByEmail(token, favoriteGameRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<FavoriteGameResponse>> getFavoriteListByEmail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        log.info("Received Request to get the list of Favorite Games");
        return ResponseEntity.ok(favoriteGameService.getFavoriteListByEmail(token));
    }

    @GetMapping("/get-game")
    public ResponseEntity<FavoriteGameResponse> getFavoriteGameByEmailAndName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("name") String name
    ) {
        log.info("Received Request to get the favorite game {}", name);
        return ResponseEntity.ok(favoriteGameService.getFavoriteGameByEmailAndName(token, name));
    }

    @DeleteMapping("/delete-game")
    public ResponseEntity<Void> deleteFavoriteGameByName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("name") String name
    ) {
        log.info("Received Request to delete the favorite game {}", name);
        favoriteGameService.deleteFavoriteGameByEmailAndName(token, name);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
