package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AllGamesGetterSteam {
    private final RestTemplate restTemplate;

    @Value("${steam.all.games.url}")
    private String allGamesUrl;

    public SteamAllGamesResponse getAllGames() {
        log.info("By url {} was sent GET request", allGamesUrl);
        SteamAllGamesResponse steamAllGamesResponse = restTemplate.getForObject(allGamesUrl, SteamAllGamesResponse.class);

        if (Objects.isNull(steamAllGamesResponse))
            return new SteamAllGamesResponse(new SteamAllGamesResponse.AppList(new ArrayList<>()));
        log.info("Was received {} games", steamAllGamesResponse.applist().apps().size());
        return steamAllGamesResponse;
    }
}
