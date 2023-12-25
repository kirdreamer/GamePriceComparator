package com.spielpreisvergleicher.common.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record SteamGameResponse(
        String type,
        String name,
        String header_image,
        Platforms platforms,
        Boolean is_free,
        String detailed_description,
        String about_the_game,
        String short_description,
        Price price_overview
) {
    public record Price(
            String currency,
            Integer initial,
            @JsonProperty("final")
            Integer final_value,
            Integer discount_percent
    ) {
    }

    public record Platforms(
            Boolean windows,
            Boolean mac,
            Boolean linux
    ) {
    }
}
