package com.spielpreisvergleicher.common.mapper;

import com.spielpreisvergleicher.common.dto.api.response.GogProduct;
import com.spielpreisvergleicher.common.dto.api.response.SteamGameResponse;
import com.spielpreisvergleicher.common.web.response.game.PlatformsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatformsMapper {
    PlatformsResponse steamPlatformsToPlatformsResponse(SteamGameResponse.Platforms platforms);
    PlatformsResponse gogPlatformsToPlatformsResponse(GogProduct.Platforms platforms);
}
