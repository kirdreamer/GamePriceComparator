package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.dto.api.response.GogProduct;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceResponse gogPriceToPriceResponse(GogProduct.Price price);
}
