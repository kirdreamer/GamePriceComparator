package com.gamepricecomparator.common.service.game.api.egs.impl;

import com.gamepricecomparator.common.dto.api.response.EgsProduct;
import com.gamepricecomparator.common.dto.api.response.EgsResponse;
import com.gamepricecomparator.common.service.game.ExternalApiService;
import com.gamepricecomparator.common.web.response.game.GameInfoResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EgsService {
    private final ExternalApiService externalApiService;
    private final GraphqlGenerator graphqlGenerator;

    @Value("${egs.base.url}")
    private String baseUrl;
    @Value("${egs.graphql.url}")
    private String graphqlUrl;

    public EgsProduct getGameById(String egsId, String name) {
        EgsResponse list = getGamesByName(name);
        return list.elements().stream()
                .filter(egsProduct -> Objects.equals(egsProduct.id(), egsId))
                .findFirst()
                .orElse(null);
    }

    public EgsResponse getGamesByName(String name) {
        //TODO Fast solution. Needs to be reworked
        String query = String.format("{\"query\": \"{" +
                "Catalog {searchStore(keywords: \\\"%s\\\", start: 0, count: 100) {" +
                "paging {count total} elements {" +
                "id keyImages{url} title urlSlug description price (country: \\\"DE\\\"){" +
                "totalPrice {originalPrice discountPrice currencyCode}}}}}}\"}", name);

        String finalUrl = String.format("%s", baseUrl + graphqlUrl);

        return externalApiService
                .getJsonFromApiGraphqlAndFindObjectByName(finalUrl, EgsResponse.class, query, "searchStore");
    }

    public void addEgsGameIntoGameList(List<EgsProduct> egsGameList, Map<String, GameResponse> gameList) {
        for (EgsProduct product : egsGameList) {
            if (gameList.containsKey(product.name())) {
                setEgsProductIntoMap(product, gameList);
                continue;
            }
            putEgsGameIntoMap(product, gameList);
        }
    }

    private void setEgsProductIntoMap(EgsProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to put the product with name {} into the List...", product.name());
        gameList.get(product.name()).setEgs(getGameInfoResponseFromEgsResponse(product));
        log.debug("Product with name {} was successfully put into the List", product.name());
    }


    private void putEgsGameIntoMap(EgsProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to add a new product with name {} into the List...", product.name());
        gameList.put(product.name(), getGameResponseFromEgsResponse(product));
        log.debug("A new product with name {} was successfully added into the List", product.name());
    }


    public GameResponse getGameResponseFromEgsResponse(EgsProduct product) {
        return GameResponse.builder()
                .name(product.name())
                .image(product.images().get(0).url())
                .short_description(product.short_description())
                .egs(getGameInfoResponseFromEgsResponse(product))
                .isFavorite(false)
                .build();
    }

    public GameInfoResponse getGameInfoResponseFromEgsResponse(EgsProduct product) {
        return new GameInfoResponse(
                product.id(),
                getPriceResponseFromEgsResponse(product.price().totalPrice()),
                baseUrl + "/p/" + product.link().replace("-releaseoffer", "")
        );
    }

    public PriceResponse getPriceResponseFromEgsResponse(EgsProduct.Price.TotalPrice totalPrice) {
        return new PriceResponse(
                keepTwoDigitAfterDecimal(totalPrice.initial_value() * 0.01d),
                keepTwoDigitAfterDecimal(totalPrice.final_value() * 0.01d),
                null,
                totalPrice.currency(),
                totalPrice.final_value().equals(0)
        );
    }

    private Double keepTwoDigitAfterDecimal(double value) {
        return Double.valueOf(String.format(Locale.US, "%.2f", value));
    }
}
