package com.gamepricecomparator.common.service.game.api.steam.impl;

import com.gamepricecomparator.common.dto.api.response.SteamAllGamesResponse;
import com.gamepricecomparator.common.dto.api.response.SteamGameResponse;
import com.gamepricecomparator.common.entity.steam.SteamGame;
import com.gamepricecomparator.common.mapper.PlatformsMapper;
import com.gamepricecomparator.common.mapper.SteamGameMapper;
import com.gamepricecomparator.common.repository.SteamGameRepository;
import com.gamepricecomparator.common.service.game.ExternalApiService;
import com.gamepricecomparator.common.web.response.game.GameInfoResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
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
    @Value("${steam.search.dlc}")
    private Boolean isSearchDlcEnabled;

    private final SteamGameMapper steamGameMapper;
    private final PlatformsMapper platformsMapper;
    private final SteamGameRepository steamGameRepository;
    private final AllGamesGetterSteam allGamesGetterSteam;

    public List<SteamGameResponse> getGamesByName(String name) {
        List<SteamGameResponse> steamGameResponses = new ArrayList<>();

        for (SteamGame steamGame : steamGameRepository.findByNameIgnoreCaseContaining(name).orElseGet(ArrayList::new)) {
            SteamGameResponse gameResponse = getGameById(steamGame.getAppid());
            if (Objects.nonNull(gameResponse) &&
                    (gameResponse.type().equals("game") ||
                            (gameResponse.type().equals("dlc") && isSearchDlcEnabled))
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
            if (!game.type().isEmpty()) {
                putSteamGameIntoMap(game, gameList);
            }
        }
        log.debug("All products from SteamGameList were added into HashMap");

        return gameList;
    }

    private void putSteamGameIntoMap(SteamGameResponse game, HashMap<String, GameResponse> gameList) {
        log.debug("Trying to add a new product with name {} into the List...", game.name());
        gameList.put(filterSymbolsInName(game.name()), getGameResponseFromSteamGameResponse(game));
        log.debug("A new product with name {} was successfully added into the List", game.name());
    }

    public GameResponse getGameResponseFromSteamGameResponse(SteamGameResponse game) {
        return GameResponse.builder()
                .name(filterSymbolsInName(game.name()))
                .type(game.type())
                .image(game.image())
                .platforms(platformsMapper.steamPlatformsToPlatformsResponse(game.platforms()))
                .about_the_game(game.about_the_game())
                .short_description(game.short_description())
                .detailed_description(game.detailed_description())
                .steam(getGameInfoResponseFromSteamGameResponse(game))
                .build();
    }

    public GameInfoResponse getGameInfoResponseFromSteamGameResponse(SteamGameResponse game) {
        return new GameInfoResponse(
                game.id(),
                getPriceResponseFromSteamGameResponse(game),
                gamePageUrl + game.id()
        );
    }

    public PriceResponse getPriceResponseFromSteamGameResponse(SteamGameResponse game) {
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
        return priceResponse;
    }

    private String filterSymbolsInName(String name) {
        return name.replaceAll("®", "");
    }

    private Double keepTwoDigitAfterDecimal(double value) {
        return Double.valueOf(String.format(Locale.US, "%.2f", value));
    }
}
