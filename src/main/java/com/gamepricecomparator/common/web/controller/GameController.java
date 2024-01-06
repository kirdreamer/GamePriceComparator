package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.game.GameService;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;

    @GetMapping("/get-all-games")
    public ResponseEntity<Void> getAllGames() {
        log.info("Received Request to get all games");
        gameService.getAllGamesAndSaveIntoDatabase();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-game-list")
    public ResponseEntity<List<GameResponse>> getGameList(@RequestParam("game") String nameGame) {
        log.info("Received Request to get a list of games with name {}", nameGame);
        return ResponseEntity.ok(gameService.getGamesByName(nameGame));
    }
}
