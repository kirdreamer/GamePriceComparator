package com.gamepricecomparator.common.web.response.game;

public record GameInfoResponse(
        Integer id,
        PriceResponse price,
        String link
) {
}
