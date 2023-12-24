package com.spielpreisvergleicher.common.service.game;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.service.game.api.gog.impl.GameGetterGog;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.AllGamesGetterSteam;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.GameGetterSteam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
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
        return;
    }
}
