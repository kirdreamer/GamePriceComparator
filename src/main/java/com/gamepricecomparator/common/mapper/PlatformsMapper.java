package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.dto.api.response.GogProduct;
import com.gamepricecomparator.common.dto.api.response.SteamGameResponse;
import com.gamepricecomparator.common.web.response.game.PlatformsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatformsMapper {
    PlatformsResponse steamPlatformsToPlatformsResponse(SteamGameResponse.Platforms platforms);
    PlatformsResponse gogPlatformsToPlatformsResponse(GogProduct.Platforms platforms);
}
