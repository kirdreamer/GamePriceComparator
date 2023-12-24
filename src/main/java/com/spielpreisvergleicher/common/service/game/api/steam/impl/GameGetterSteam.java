package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import com.spielpreisvergleicher.common.mapper.SteamGameMapper;
import com.spielpreisvergleicher.common.repository.SteamGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameGetterSteam {
    private final SteamGameMapper steamGameMapper;
    private final SteamGameRepository steamGameRepository;
    private final AllGamesGetterSteam allGamesGetterSteam;

    public List<SteamGame> getGamesByName(String name) {
        return steamGameRepository.findByNameIgnoreCaseContaining(name).orElseGet(ArrayList::new);
    }

    public void getGameById(Integer id) {

    }

    public void getAllSteamGamesAndSaveIntoDatabase() {
        Set<SteamGame> steamGameList = new HashSet<>();

        for (SteamAllGamesResponse.AppList.App app : allGamesGetterSteam.getAllGames().applist().apps())
            steamGameList.add(steamGameMapper.appToSteamGame(app));

        log.info("Trying to save all received games from steam into database...");
        steamGameRepository.saveAll(steamGameList);
        log.info("All information was successfully saved");
    }
}
