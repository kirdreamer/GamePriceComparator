package com.spielpreisvergleicher.common.web.controller;

import com.spielpreisvergleicher.common.web.request.NewFavoriteGameRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    @PostMapping("/add-favorite")
    public ResponseEntity<Void> addFavoriteGameToUser(
            @RequestBody NewFavoriteGameRequest newFavoriteGameRequest
    ) {
        log.info("Received Request to add a new Favorite Game with name {} from user {}",
                newFavoriteGameRequest.favoriteGame().name(), newFavoriteGameRequest.email());

        return ResponseEntity.ok().build();
    }
}
