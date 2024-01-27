package com.gamepricecomparator.common.service.game;

import com.gamepricecomparator.common.dto.GameDTO;
import com.gamepricecomparator.common.dto.api.response.EgsResponse;
import com.gamepricecomparator.common.dto.api.response.GogProduct;
import com.gamepricecomparator.common.dto.api.response.SteamGameResponse;
import com.gamepricecomparator.common.service.game.api.egs.impl.EgsService;
import com.gamepricecomparator.common.service.game.api.gog.impl.GogService;
import com.gamepricecomparator.common.service.game.api.steam.impl.SteamService;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final SteamService steamService;
    private final EgsService egsService;
    private final GogService gogService;

    public List<GameResponse> getGamesByName(String name) {
        List<EgsResponse> egsGames = egsService.getGamesByName(name);
        log.info("Received {} products from Epic Games Store", egsGames.size());

        List<GogProduct> gogGames = gogService.getGamesByName(name);
        log.info("Received {} products from GOG", gogGames.size());

        List<SteamGameResponse> steamGames = steamService.getGamesByName(name);
        log.info("Received {} products from Steam", steamGames.size());

        Map<String, GameResponse> gameList = steamService.prepareGameListFromSteam(steamGames);
        gogService.addGogGameIntoGameList(gogGames, gameList);
        log.info("Map gameList contains {} products", gameList.size());

        return new ArrayList<>(gameList.values());
    }

    public GameResponse getSpecificGameByIdAndName(GameDTO gameDTO) {
        GameResponse gameResponse = null;

        if (Objects.nonNull(gameDTO.steamId())) {
            gameResponse = steamService.getGameResponseFromSteamGameResponse(
                    steamService.getGameById(gameDTO.steamId()));
            if (Objects.nonNull(gameDTO.gogId())) {
                gameResponse.setGog(gogService.getGameInfoResponseFromGogProduct(
                        gogService.getGameById(gameDTO.gogId(), gameDTO.name())));
            }
        } else if (Objects.nonNull(gameDTO.gogId())) {
            gameResponse = gogService.getGameResponseFromGogProduct(
                    gogService.getGameById(gameDTO.gogId(), gameDTO.name()));
        }

        return gameResponse;
    }

    public List<GameResponse> getSpecificGamesListFromList(List<GameDTO> games) {
        List<GameResponse> specificGames = new ArrayList<>();

        for (GameDTO game : games) {
            specificGames.add(getSpecificGameByIdAndName(game));
        }

        return specificGames;
    }

}
