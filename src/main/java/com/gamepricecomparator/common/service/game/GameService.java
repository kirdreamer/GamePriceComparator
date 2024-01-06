package com.gamepricecomparator.common.service.game;

import com.gamepricecomparator.common.dto.api.response.GogProduct;
import com.gamepricecomparator.common.dto.api.response.SteamGameResponse;
import com.gamepricecomparator.common.service.game.api.gog.impl.GogService;
import com.gamepricecomparator.common.service.game.api.steam.impl.SteamService;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final SteamService steamService;
    private final GogService gogService;

    public List<GameResponse> getGamesByName(String name) {
        List<GogProduct> gogGames = gogService.getGamesByName(name);
        log.info("Received {} products from GOG", gogGames.size());

        List<SteamGameResponse> steamGames = steamService.getGamesByName(name);
        log.info("Received {} products from Steam", steamGames.size());

        Map<String, GameResponse> gameList = steamService.prepareGameListFromSteam(steamGames);
        gogService.addGogGameIntoGameList(gogGames, gameList);
        log.info("Map gameList contains {} products", gameList.size());

        return new ArrayList<>(gameList.values());
    }

    public void getAllGamesAndSaveIntoDatabase() {
        steamService.getAllSteamGamesAndSaveIntoDatabase();
    }

}
