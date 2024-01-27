package com.gamepricecomparator.common.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EgsResponse(
        Integer id,
        @JsonProperty("keyImages")
        List<Url> images,
        @JsonProperty("title")
        String name,
        @JsonProperty("urlSlug")
        String urlSlug,
        @JsonProperty("description")
        String short_description,
        Price price
) {
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

