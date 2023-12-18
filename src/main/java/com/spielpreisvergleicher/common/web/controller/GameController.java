package com.spielpreisvergleicher.common.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameController {

    @GetMapping("/get-all-games")
    public List<String> getAllGames() {
        log.info("Received Request to get all games");
        List<String> testList = new ArrayList<>();
        testList.add("blabla");
        testList.add("blabl1a");
        testList.add("blabl123a");
        testList.add("blab3l123a");
        return testList;
    }

    @GetMapping("/get-game-list")
    public List<String> getGameList(@RequestParam("game") String nameGame) {
        log.info("Received Request to get a list of games with name {}", nameGame);
        List<String> testList = new ArrayList<>();
        testList.add("blabla");
        testList.add("blabl1a");
        testList.add("blabl12a");
        return testList;
    }
    @GetMapping("/get-game")
    public String getGame(@RequestParam("game") String nameGame) {
        log.info("Received Request to get games with name {}", nameGame);
        return "blabl12a";
    }
}
