package com.spielpreisvergleicher.common.service.game.api.gog.impl;

import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.service.game.api.GameGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component("gogGetter")
@RequiredArgsConstructor
@Slf4j
public class GameGetterGogImpl implements GameGetter {
    private final RestTemplate restTemplate;

    @Value("${gog.base.products.url}")
    private String productsUrl;

    @Override
    public void getGamesByName(String name) {
        String parameters = "mediaType=game&search=";
        String finalUrl = String.format("%s%s%s", productsUrl, parameters, URLEncoder.encode(name, StandardCharsets.UTF_8));
        log.info("By url {} was sent request GET request", finalUrl);
        GogResponse gogResponse = restTemplate.getForObject(finalUrl, GogResponse.class);
        if (Objects.isNull(gogResponse))
            return;
        log.info("Was received {} products", gogResponse.products().size());
    }

    @Override
    public void getGameById(Integer id) {

    }
}
