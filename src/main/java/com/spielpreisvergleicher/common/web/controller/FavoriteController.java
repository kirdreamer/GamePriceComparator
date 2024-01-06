package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.service.FavoriteGameService;
import com.spielpreisvergleicher.common.web.request.FavoriteGameRequest;
import com.spielpreisvergleicher.common.web.response.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
