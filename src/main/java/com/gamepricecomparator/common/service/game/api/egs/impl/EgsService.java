package com.gamepricecomparator.common.service.game.api.egs.impl;

import com.gamepricecomparator.common.dto.api.response.EgsResponse;
import com.gamepricecomparator.common.service.game.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    public List<EgsResponse> getGamesByName(String name) {
        //TODO Fast solution. Needs to be reworked
        String query = String.format("{\"query\": \"{" +
                "Catalog {searchStore(keywords: \\\"%s\\\", start: 0, count: 100) {" +
                "paging {count total} elements {" +
                "id keyImages{url} title urlSlug description price (country: \\\"DE\\\"){" +
                "totalPrice {originalPrice discountPrice currencyCode}}}}}}\"}", name);

        String finalUrl = String.format("%s", baseUrl + graphqlUrl);

        return (List<EgsResponse>) externalApiService
                .getJsonFromApiGraphqlAndFindObjectByName(finalUrl, List.class, query, "elements");
    }
}
