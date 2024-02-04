package com.gamepricecomparator.common.web.response.game;

import com.gamepricecomparator.common.constant.Platfrom;

public record GameProviderResponse(
        Platfrom name,
        String id,
        PriceResponse price,
        String link
) {
}
