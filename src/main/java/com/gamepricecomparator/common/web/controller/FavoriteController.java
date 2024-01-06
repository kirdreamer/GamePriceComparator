package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.FavoriteGameService;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @RequestBody FavoriteGameRequest favoriteGameRequest
    ) {
        log.info("Received Request to add a new Favorite Game with name {} from user {}",
                favoriteGameRequest.name(), favoriteGameRequest.email());
        favoriteGameService.saveFavoriteGameByEmail(favoriteGameRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<FavoriteGameResponse>> getFavoriteListByEmail(@RequestParam("email") String email) {
        log.info("Received Request to get the list of Favorite Games for user {}", email);
        return ResponseEntity.ok(favoriteGameService.getFavoriteListByEmail(email));
    }

    @GetMapping("/get-game")
    public ResponseEntity<FavoriteGameResponse> getFavoriteGameByEmailAndName(
            @RequestParam("email") String email,
            @RequestParam("name") String name
    ) {
        log.info("Received Request to get the favorite game {} for user {}", name, email);
        return ResponseEntity.ok(favoriteGameService.getFavoriteGameByEmailAndName(email, name));
    }

    @DeleteMapping("/delete-game")
    public ResponseEntity<Void> deleteFavoriteGameByEmailAndName(
            @RequestParam("email") String email,
            @RequestParam("name") String name
    ) {
        log.info("Received Request to delete the favorite game {} for user {}", name, email);
        favoriteGameService.deleteFavoriteGameByEmailAndName(email, name);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
