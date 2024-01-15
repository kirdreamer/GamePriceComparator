package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.service.game.GameService;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;

    @GetMapping("/search")
    public ResponseEntity<List<GameResponse>> getGameList(@RequestParam("name") String gameName) {
        log.info("Received Request to search for game with name {}", gameName);
        return ResponseEntity.ok(gameService.getGamesByName(gameName));
    }
}
