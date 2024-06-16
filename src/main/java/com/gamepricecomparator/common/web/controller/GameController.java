package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.favorite.FavoriteGameService;
import com.gamepricecomparator.common.service.game.GameService;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/games")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final FavoriteGameService favoriteGameService;

    @GetMapping("/search")
    public ResponseEntity<List<GameResponse>> getGameList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("name") String gameName
    ) {
        log.info("Received Request to search for game with name {}", gameName);
        List<GameResponse> games = gameService.getGamesByName(gameName);

        if (Objects.nonNull(token))
            favoriteGameService.setAllFavoriteGamesInListAsFavoriteGame(games, token);

        return ResponseEntity.ok(games);
    }
}
