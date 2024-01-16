package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.favorite.FavoriteGameService;
import com.gamepricecomparator.common.service.favorite.FavoriteListEmailSenderService;
import com.gamepricecomparator.common.service.game.GameService;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.game.GameResponse;
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
    private final GameService gameService;
    private final FavoriteListEmailSenderService favoriteListEmailSenderService;

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
    public ResponseEntity<List<GameResponse>> getFavoriteListByEmail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        log.info("Received Request to get the list of Favorite Games");
        List<GameResponse> games =
                gameService.getSpecificGamesListFromList(
                        favoriteGameService.getFavoriteGamesByEmail(token).values().stream().toList());

        favoriteGameService.setAllGamesInListAsFavoriteGame(games);

        return ResponseEntity.ok(games);
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


    @GetMapping("/send-price-alarm-emails")
    public ResponseEntity<Void> sendPriceAlarmEmails() {
        log.info("Received Request to send price alarm emails if there are any sales of Favorite Games");
        favoriteListEmailSenderService.sendPriceAlarmEmail();
        return ResponseEntity.ok().build();
    }
}
