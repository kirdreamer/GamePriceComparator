package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.spielpreisvergleicher.common.dto.api.response.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AllGamesGetterSteam {
    private final ExternalApiService externalApiService;

    @Value("${steam.all.games.url}")
    private String allGamesUrl;

    public SteamAllGamesResponse getAllGames() {
        SteamAllGamesResponse steamAllGamesResponse =
                externalApiService.makeGetRequest(allGamesUrl, SteamAllGamesResponse.class);

        if (Objects.isNull(steamAllGamesResponse))
            return new SteamAllGamesResponse(new SteamAllGamesResponse.AppList(new ArrayList<>()));
        log.info("Was received {} games from global Steam list", steamAllGamesResponse.applist().apps().size());
        return steamAllGamesResponse;
    }
}
