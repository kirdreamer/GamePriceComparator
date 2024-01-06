package com.gamepricecomparator.common.dto.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public record SteamGameResponse(
        String type,
        String name,
        @JsonProperty("steam_appid")
        Integer id,
        @JsonProperty("header_image")
        String image,
        Platforms platforms,
        Boolean is_free,
        String detailed_description,
        String about_the_game,
        String short_description,
        @JsonProperty("price_overview")
        Price price
) {
    public record Price(
            @JsonProperty("initial")
            Integer initial_value,
            @JsonProperty("final")
            Integer final_value,
            Integer discount_percent,
            String currency
    ) {
    }

    public record Platforms(
            Boolean windows,
            Boolean mac,
            Boolean linux
    ) {
    }
}
