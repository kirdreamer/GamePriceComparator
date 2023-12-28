package com.spielpreisvergleicher.common.mapper;

import com.spielpreisvergleicher.common.dto.api.response.GogProduct;
import com.spielpreisvergleicher.common.web.response.game.PriceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceResponse gogPriceToPriceResponse(GogProduct.Price price);
}
