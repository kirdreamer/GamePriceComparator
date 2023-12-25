package com.spielpreisvergleicher.common.service.game;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.dto.SteamGameResponse;
import com.spielpreisvergleicher.common.service.game.api.gog.impl.GameGetterGog;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.GameGetterSteam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameGetterSteam gameGetterSteam;
    private final GameGetterGog gameGetterGog;

    public void getGamesByName(String name) { //TODO change to GameResponse
        GogResponse gogResponse = gameGetterGog.getGamesByName(name);

        List<SteamGameResponse> steamGames = gameGetterSteam.getGamesByName(name);
        return;
        //TODO combine common games
        //TODO create list Response for each found game
    }

    public void getAllGamesAndSaveIntoDatabase() {
        gameGetterSteam.getAllSteamGamesAndSaveIntoDatabase();
    }
}
