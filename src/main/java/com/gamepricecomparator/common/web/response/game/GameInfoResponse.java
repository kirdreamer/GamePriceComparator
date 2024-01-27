package com.gamepricecomparator.common.web.response.game;

public record GameInfoResponse(
        String id,
        PriceResponse price,
        String link
) {
}
