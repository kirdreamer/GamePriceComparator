package com.spielpreisvergleicher.common.service.game;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import com.spielpreisvergleicher.common.mapper.SteamGameMapper;
import com.spielpreisvergleicher.common.service.game.api.gog.impl.GameGetterGog;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.AllGamesGetterSteam;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.GameGetterSteam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SteamGameMapper steamGameMapper;
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
        SteamAllGamesResponse steamAllGamesResponse = allGamesGetterSteam.getAllGames();
        List<SteamGame> steamGameList = new ArrayList<>();
        for (SteamAllGamesResponse.AppList.App app : steamAllGamesResponse.applist().apps()) {
            steamGameList.add(steamGameMapper.appToSteamGame(app));
        }
        return;
    }
}
