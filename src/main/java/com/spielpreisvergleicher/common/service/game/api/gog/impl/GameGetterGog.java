package com.spielpreisvergleicher.common.service.game.api.gog.impl;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameGetterGog {
    private final ExternalApiService externalApiService;

    @Value("${gog.base.products.url}")
    private String productsUrl;

    public GogResponse getGamesByName(String name) {
        String parameters = String.format("?mediaType=game&search=%s",
                URLEncoder.encode(name, StandardCharsets.UTF_8));
        String finalUrl = String.format("%s%s", productsUrl, parameters);

        GogResponse gogResponse = externalApiService.makeGetRequest(finalUrl, GogResponse.class);

        if (Objects.isNull(gogResponse))
            return new GogResponse(new ArrayList<>());
        log.info("Was received {} products", gogResponse.products().size());
        return gogResponse;
    }

    public void getGameById(Integer id) {

    }
}
