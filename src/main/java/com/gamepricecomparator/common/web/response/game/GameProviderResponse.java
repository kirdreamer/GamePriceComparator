package com.gamepricecomparator.common.web.response.game;

import com.gamepricecomparator.common.constant.Platform;

public record GameProviderResponse(
        Platform name,
        String id,
        PriceResponse price,
        String link
) {
}
