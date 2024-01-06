package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.dto.api.response.SteamAllGamesResponse;
import com.gamepricecomparator.common.entity.steam.SteamGame;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SteamGameMapper {
    SteamGame appToSteamGame(SteamAllGamesResponse.AppList.App apps);
}
