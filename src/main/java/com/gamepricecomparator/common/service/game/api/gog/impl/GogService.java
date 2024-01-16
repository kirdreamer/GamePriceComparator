package com.gamepricecomparator.common.service.game.api.gog.impl;

import com.gamepricecomparator.common.dto.api.response.GogProduct;
import com.gamepricecomparator.common.dto.api.response.GogResponse;
import com.gamepricecomparator.common.mapper.PlatformsMapper;
import com.gamepricecomparator.common.mapper.PriceMapper;
import com.gamepricecomparator.common.service.game.ExternalApiService;
import com.gamepricecomparator.common.web.response.game.GameInfoResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public GogProduct getGameById(Integer gogId, String name) {
        List<GogProduct> list = getGamesByName(name);
        return list.stream()
                .filter(gogProduct -> Objects.equals(gogProduct.id(), gogId))
                .findFirst()
                .orElse(null);
    }

    public List<GogProduct> getGamesByName(String name) {
        String parameters = String.format("?search=%s", name);
        String finalUrl = String.format("%s%s", baseUrl + productsUrl, parameters);
        return externalApiService.makeGetRequest(finalUrl, GogResponse.class).products().stream()
                .filter(product -> Arrays.stream(Strings.delimitedListToStringArray(name.toLowerCase(), " "))
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
        log.debug("Trying to put the product with name {} into the List...", product.name());
        gameList.get(product.name()).setGog(getGameInfoResponseFromGogProduct(product));
        log.debug("Product with name {} was successfully put into the List", product.name());
    }

    private void putGogGameIntoMap(GogProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to add a new product with name {} into the List...", product.name());
        gameList.put(product.name(), getGameResponseFromGogProduct(product));
        log.debug("A new product with name {} was successfully added into the List", product.name());
    }

    public GameResponse getGameResponseFromGogProduct(GogProduct product) {
        return GameResponse.builder()
                .name(product.name())
                .image("https:" + product.image() + ".jpg")
                .platforms(platformsMapper.gogPlatformsToPlatformsResponse(product.platforms()))
                .gog(getGameInfoResponseFromGogProduct(product))
                .isFavorite(false)
                .build();
    }

    public GameInfoResponse getGameInfoResponseFromGogProduct(GogProduct product) {
        return new GameInfoResponse(
                product.id(),
                getPriceResponseFromGogProduct(product),
                baseUrl + product.link()
        );
    }

    public PriceResponse getPriceResponseFromGogProduct(GogProduct product) {
        return priceMapper.gogPriceToPriceResponse(product.price());
    }
}
