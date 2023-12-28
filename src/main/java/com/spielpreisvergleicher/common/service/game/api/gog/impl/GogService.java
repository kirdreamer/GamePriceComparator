package com.spielpreisvergleicher.common.service.game.api.gog.impl;

import com.spielpreisvergleicher.common.dto.api.response.GogProduct;
import com.spielpreisvergleicher.common.dto.api.response.GogResponse;
import com.spielpreisvergleicher.common.mapper.PlatformsMapper;
import com.spielpreisvergleicher.common.mapper.PriceMapper;
import com.spielpreisvergleicher.common.service.game.ExternalApiService;
import com.spielpreisvergleicher.common.web.response.game.GameInfoResponse;
import com.spielpreisvergleicher.common.web.response.game.GameResponse;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GogService {
    private final ExternalApiService externalApiService;
    private final PriceMapper priceMapper;
    private final PlatformsMapper platformsMapper;

    @Value("${gog.base.products.url}")
    private String productsUrl;
    @Value("${gog.base.url}")
    private String baseUrl;

    public List<GogProduct> getGamesByName(String name) {
        String parameters = String.format("?search=%s", URLEncoder.encode(name, StandardCharsets.UTF_8));
        String finalUrl = String.format("%s%s", baseUrl + productsUrl, parameters);
        return externalApiService.makeGetRequest(finalUrl, GogResponse.class).products().stream()
                .filter(product -> Arrays.stream(Strings.delimitedListToStringArray(name, " "))
                        .allMatch(product.name().toLowerCase()::contains))
                .toList();
    }

    public void addGogGameIntoGameList(List<GogProduct> gogGameList, Map<String, GameResponse> gameList) {
        for (GogProduct product : gogGameList) {
            if (gameList.containsKey(product.name())) {
                setGogProductIntoMap(product, gameList);
                continue;
            }
            putGogGameIntoMap(product, gameList);
        }
    }

    private void setGogProductIntoMap(GogProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to put product with name {} into the List...", product.name());
        gameList.get(product.name()).setGog(new GameInfoResponse(
                product.id(),
                priceMapper.gogPriceToPriceResponse(product.price()),
                baseUrl + product.link())
        );

        log.debug("Product with name {} was successfully put into the List", product.name());
    }

    private void putGogGameIntoMap(GogProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to add a new product with name {} into the List...", product.name());
        gameList.put(product.name(),
                GameResponse.builder()
                        .name(product.name())
                        .image(product.image()) //TODO Required rework
                        .platforms(platformsMapper.gogPlatformsToPlatformsResponse(product.platforms()))
                        .gog(new GameInfoResponse(
                                product.id(),
                                //prices in steam presented as integer value
                                priceMapper.gogPriceToPriceResponse(product.price()),
                                baseUrl + product.link()
                        ))
                        .build()
        );
        log.debug("A new product with name {} was successfully added into the List", product.name());
    }
}
