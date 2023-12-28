package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.spielpreisvergleicher.common.dto.api.response.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.dto.api.response.SteamGameResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import com.spielpreisvergleicher.common.mapper.PlatformsMapper;
import com.spielpreisvergleicher.common.mapper.SteamGameMapper;
import com.spielpreisvergleicher.common.repository.SteamGameRepository;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import com.spielpreisvergleicher.common.web.response.game.GameInfoResponse;
import com.spielpreisvergleicher.common.web.response.game.GameResponse;
import com.spielpreisvergleicher.common.web.response.game.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SteamService {
    private final ExternalApiService externalApiService;

    @Value("${steam.certain.game.url}")
    private String gameUrl;
    @Value("${steam.game.page.url}")
    private String gamePageUrl;

    private final SteamGameMapper steamGameMapper;
    private final PlatformsMapper platformsMapper;
    private final SteamGameRepository steamGameRepository;
    private final AllGamesGetterSteam allGamesGetterSteam;

    public List<SteamGameResponse> getGamesByName(String name) {
        List<SteamGameResponse> steamGameResponses = new ArrayList<>();

        for (SteamGame steamGame : steamGameRepository.findByNameIgnoreCaseContaining(name).orElseGet(ArrayList::new)) {
            SteamGameResponse gameResponse = getGameById(steamGame.getAppid());
            if (Objects.nonNull(gameResponse) &&
                    (gameResponse.type().equals("game") || gameResponse.type().equals("dlc"))
            )
                steamGameResponses.add(gameResponse);
        }

        return steamGameResponses;
    }

    public SteamGameResponse getGameById(Integer id) {
        String arguments = String.format("?appids=%s&cc=de", id);
        String finalUrl = String.format("%s%s", gameUrl, arguments);
        return externalApiService.getJsonFromApiAndFindObjectByName(finalUrl, SteamGameResponse.class, "data");
    }

    public void getAllSteamGamesAndSaveIntoDatabase() {
        List<SteamGame> steamGameList = new ArrayList<>();

        for (SteamAllGamesResponse.AppList.App app : allGamesGetterSteam.getAllGames().applist().apps())
            steamGameList.add(steamGameMapper.appToSteamGame(app));

        log.info("Trying to save all received games from steam into database...");
        steamGameRepository.saveAll(steamGameList);
        log.info("All information was successfully saved");
    }

    public HashMap<String, GameResponse> prepareGameListFromSteam(List<SteamGameResponse> steamGameList) {
        HashMap<String, GameResponse> gameList = new HashMap<>();

        log.debug("Trying to add all products from SteamGameList into HashMap...");
        for (SteamGameResponse game : steamGameList) {
            putSteamGameIntoMap(game, gameList);
        }
        log.debug("All products from SteamGameList were added into HashMap");

        return gameList;
    }

    private Double keepTwoDigitAfterDecimal(double value) {
        return Double.valueOf(String.format(Locale.US, "%.2f", value));
    }

    private void putSteamGameIntoMap(SteamGameResponse game, HashMap<String, GameResponse> gameList) {
        PriceResponse priceResponse = null;
        if (Objects.nonNull(game.price())) {
            priceResponse = new PriceResponse(
                    //prices in steam presented as integer value
                    keepTwoDigitAfterDecimal(game.price().initial_value() * 0.01d),
                    keepTwoDigitAfterDecimal(game.price().final_value() * 0.01d),
                    game.price().discount_percent(),
                    game.price().currency(),
                    game.is_free()
            );
        } else if (game.is_free()) {
            priceResponse = new PriceResponse(
                    0.0,
                    0.0,
                    0,
                    "EUR",
                    true);
        }

        gameList.put(filterSymbolsInName(game.name()),
                GameResponse.builder()
                        .name(filterSymbolsInName(game.name()))
                        .type(game.type())
                        .image(game.image())
                        .platforms(platformsMapper.steamPlatformsToPlatformsResponse(game.platforms()))
                        .about_the_game(game.about_the_game())
                        .short_description(game.short_description())
                        .detailed_description(game.detailed_description())
                        .steam(new GameInfoResponse(
                                game.id(),
                                priceResponse,
                                gamePageUrl + game.id()
                        ))
                        .build()
        );
    }

    private String filterSymbolsInName(String name) {
        return name.replaceAll("®", "");
    }
}
