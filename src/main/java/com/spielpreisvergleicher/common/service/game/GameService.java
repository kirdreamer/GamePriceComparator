package com.spielpreisvergleicher.common.service.game;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.service.game.api.gog.impl.GameGetterGogImpl;
import com.spielpreisvergleicher.common.service.game.api.steam.impl.GameGetterSteamImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameGetterSteamImpl gameGetterSteam;
    private final GameGetterGogImpl gameGetterGog;

    public void getGamesByName(String name) { //TODO change to GameResponse
        GogResponse gogResponse = gameGetterGog.getGamesByName(name);
        //TODO make SteamImpl
        //TODO combine common games
        //TODO create list Response for each found game
    }
}
