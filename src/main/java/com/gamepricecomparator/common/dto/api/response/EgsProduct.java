package com.gamepricecomparator.common.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EgsProduct(
        String id,
        @JsonProperty("keyImages")
        List<Url> images,
        @JsonProperty("title")
        String name,
        @JsonProperty("productSlug")
        String link,
        List<OfferMapping> offerMappings,
        @JsonProperty("description")
        String short_description,
        Price price
) {
    public record OfferMapping(String pageSlug) {
    }

    public record Url(String url) {
    }

    public record Price(TotalPrice totalPrice) {
        public record TotalPrice(
                @JsonProperty("originalPrice")
                Integer initial_value,
                @JsonProperty("discountPrice")
                Integer final_value,
                @JsonProperty("currencyCode")
                String currency
        ) {
        }
    }
}

