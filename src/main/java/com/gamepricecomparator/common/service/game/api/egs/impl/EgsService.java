package com.gamepricecomparator.common.service.game.api.egs.impl;

import com.gamepricecomparator.common.constant.Platfrom;
import com.gamepricecomparator.common.dto.api.response.EgsProduct;
import com.gamepricecomparator.common.dto.api.response.EgsResponse;
import com.gamepricecomparator.common.service.game.ExternalApiService;
import com.gamepricecomparator.common.web.response.game.GameProviderResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.gamepricecomparator.common.service.UtilService.filterSymbolsInName;
import static com.gamepricecomparator.common.service.UtilService.keepTwoDigitAfterDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EgsService {
    private final ExternalApiService externalApiService;

    @Value("${egs.base.url}")
    private String baseUrl;
    @Value("${egs.graphql.url}")
    private String graphqlUrl;

    public EgsProduct getGameById(String egsId, String name) {
        EgsResponse list = getGamesByName(filterSymbolsInName(name));
        return list.elements().stream()
                .filter(egsProduct -> Objects.equals(egsProduct.id(), egsId))
                .findFirst()
                .orElse(null);
    }

    public EgsResponse getGamesByName(String name) {
        String query = new GraphqlEgsBuilder().setKeywords(name).build();

        String finalUrl = String.format("%s", baseUrl + graphqlUrl);

        return externalApiService
                .getJsonFromApiGraphqlAndFindObjectByName(finalUrl, EgsResponse.class, query, "searchStore");
    }

    public void addEgsGameIntoGameList(List<EgsProduct> egsGameList, Map<String, GameResponse> gameList) {
        for (EgsProduct product : egsGameList) {
            if (gameList.containsKey(filterSymbolsInName(product.name()))) {
                setEgsProductIntoMap(product, gameList);
                continue;
            }
            putEgsGameIntoMap(product, gameList);
        }
    }

    private void setEgsProductIntoMap(EgsProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to put the product with name {} into the List...", product.name());
        gameList.get(filterSymbolsInName(product.name())).game_providers.add(getGameInfoResponseFromEgsResponse(product));
        log.debug("Product with name {} was successfully put into the List", product.name());
    }


    private void putEgsGameIntoMap(EgsProduct product, Map<String, GameResponse> gameList) {
        log.debug("Trying to add a new product with name {} into the List...", product.name());
        gameList.put(filterSymbolsInName(product.name()), getGameResponseFromEgsResponse(product));
        log.debug("A new product with name {} was successfully added into the List", product.name());
    }


    public GameResponse getGameResponseFromEgsResponse(EgsProduct product) {
        GameResponse gameResponse = GameResponse.builder()
                .name(product.name())
                .image(product.images().get(0).url())
                .short_description(product.short_description())
                .game_providers(new ArrayList<>())
                .is_favorite(false)
                .build();
        gameResponse.game_providers.add(getGameInfoResponseFromEgsResponse(product));
        return gameResponse;
    }

    public GameProviderResponse getGameInfoResponseFromEgsResponse(EgsProduct product) {
        return new GameProviderResponse(
                Platfrom.EPIC_GAMES_STORE,
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
}
