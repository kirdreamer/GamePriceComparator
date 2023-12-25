package com.spielpreisvergleicher.common.service.game.api.gog.impl;

import com.spielpreisvergleicher.common.dto.GogProducts;
import com.spielpreisvergleicher.common.dto.GogResponse;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameGetterGog {
    private final ExternalApiService externalApiService;

    @Value("${gog.base.products.url}")
    private String productsUrl;

    public List<GogProducts> getGamesByName(String name) {
        String parameters = String.format("?search=%s", URLEncoder.encode(name, StandardCharsets.UTF_8));
        String finalUrl = String.format("%s%s", productsUrl, parameters);
        return externalApiService.makeGetRequest(finalUrl, GogResponse.class).products().stream()
                .filter(product -> Arrays.stream(Strings.delimitedListToStringArray(name, " "))
                                .allMatch(product.title()::contains))
                .toList();
    }
}
