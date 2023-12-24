package com.spielpreisvergleicher.common.service.game;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import com.spielpreisvergleicher.common.mapper.SteamGameMapper;
import com.spielpreisvergleicher.common.repository.SteamGameRepository;
import com.spielpreisvergleicher.common.service.game.api.gog.impl.GameGetterGog;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.AllGamesGetterSteam;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.GameGetterSteam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final SteamGameMapper steamGameMapper;
    private final SteamGameRepository steamGameRepository;
    private final GameGetterSteam gameGetterSteam;
    private final AllGamesGetterSteam allGamesGetterSteam;
    private final GameGetterGog gameGetterGog;

    public void getGamesByName(String name) { //TODO change to GameResponse
        GogResponse gogResponse = gameGetterGog.getGamesByName(name);
        //TODO make SteamImpl
        //TODO combine common games
        //TODO create list Response for each found game
    }

    public void getAllSteamGamesAndSaveIntoDatabase() {
        List<SteamGame> steamGameList = new ArrayList<>();

        for (SteamAllGamesResponse.AppList.App app : allGamesGetterSteam.getAllGames().applist().apps())
            steamGameList.add(steamGameMapper.appToSteamGame(app));

        log.info("Trying to save all received games from steam into database...");
        steamGameRepository.saveAll(steamGameList);
        log.info("All information was successfully saved");
    }
}
