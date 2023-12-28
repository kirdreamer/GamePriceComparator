package com.spielpreisvergleicher.common.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GogProduct(
        @JsonProperty("title")
        String name,
        Integer id,
        Price price,
        @JsonProperty("url")
        String link,
        @JsonProperty("worksOn")
        Platforms platforms,
        String image
) {
    public record Price(
            @JsonProperty("baseAmount")
            Double initial_value,
            @JsonProperty("finalAmount")
            Double final_value,
            @JsonProperty("discountPercentage")
            Integer discount_percent,
            String currency,
            @JsonProperty("isFree")
            Boolean is_free
    ) {
    }

    public record Platforms(
            @JsonProperty("Windows")
            Boolean windows,
            @JsonProperty("Mac")
            Boolean mac,
            @JsonProperty("Linux")
            Boolean linux
    ) {
    }
}
