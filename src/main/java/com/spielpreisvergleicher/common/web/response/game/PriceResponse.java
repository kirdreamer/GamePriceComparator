package com.spielpreisvergleicher.common.web.response.game;

public record PriceResponse(
        Double initial_value,
        Double final_value,
        Integer discount_percent,
        String currency,
        Boolean is_free
) {
}
