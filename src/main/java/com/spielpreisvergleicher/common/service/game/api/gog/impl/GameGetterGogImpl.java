package com.spielpreisvergleicher.common.service.game.api.gog.impl;

import com.spielpreisvergleicher.common.dto.GogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameGetterGogImpl {
    private final RestTemplate restTemplate;

    @Value("${gog.base.products.url}")
    private String productsUrl;

    public GogResponse getGamesByName(String name) {
        String parameters = "mediaType=game&search=";
        String finalUrl = String.format("%s%s%s", productsUrl, parameters, URLEncoder.encode(name, StandardCharsets.UTF_8));
        log.info("By url {} was sent request GET request", finalUrl);
        GogResponse gogResponse = restTemplate.getForObject(finalUrl, GogResponse.class);
        if (Objects.isNull(gogResponse))
            return new GogResponse(new ArrayList<>());
        log.info("Was received {} products", gogResponse.products().size());
        return gogResponse;
    }

    public void getGameById(Integer id) {

    }
}
