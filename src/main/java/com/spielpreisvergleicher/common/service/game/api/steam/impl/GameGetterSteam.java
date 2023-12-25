package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spielpreisvergleicher.common.dto.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.dto.SteamGameResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import com.spielpreisvergleicher.common.mapper.SteamGameMapper;
import com.spielpreisvergleicher.common.repository.SteamGameRepository;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameGetterSteam {
    private final ExternalApiService externalApiService;
    private final ObjectMapper objectMapper;

    @Value("${steam.certain.game.url}")
    private String gameUrl;

    private final SteamGameMapper steamGameMapper;
    private final SteamGameRepository steamGameRepository;
    private final AllGamesGetterSteam allGamesGetterSteam;

    public List<SteamGameResponse> getGamesByName(String name) {
        List<SteamGame> steamGameList = steamGameRepository.findByNameIgnoreCaseContaining(name)
                .orElseGet(ArrayList::new);

        List<SteamGameResponse> steamGameResponses = new ArrayList<>();
        for (SteamGame steamGame : steamGameList)
            steamGameResponses.add(getGameById(steamGame.getAppid()));

        return steamGameResponses;
    }

    public SteamGameResponse getGameById(Integer id) {
        String arguments = String.format("?appids=%s&cc=de", id);
        String finalUrl = String.format("%s%s", gameUrl, arguments);
        SteamGameResponse steamGameResponse;
        try {
            steamGameResponse = objectMapper.treeToValue(
                    objectMapper.readTree(externalApiService.makeGetRequest(finalUrl, String.class))
                            .findValue("data"),
                    SteamGameResponse.class
            );
        } catch (JsonProcessingException e) {
            log.error("An error occurs during the mapping: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return steamGameResponse;
    }

    public void getAllSteamGamesAndSaveIntoDatabase() {
        List<SteamGame> steamGameList = new ArrayList<>();

        for (SteamAllGamesResponse.AppList.App app : allGamesGetterSteam.getAllGames().applist().apps())
            steamGameList.add(steamGameMapper.appToSteamGame(app));

        log.info("Trying to save all received games from steam into database...");
        steamGameRepository.saveAll(steamGameList);
        log.info("All information was successfully saved");
    }
}
