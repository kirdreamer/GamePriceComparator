package com.spielpreisvergleicher.common.mapper;

import com.spielpreisvergleicher.common.dto.api.response.SteamAllGamesResponse;
import com.spielpreisvergleicher.common.entity.steam.SteamGame;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SteamGameMapper {
    SteamGame appToSteamGame(SteamAllGamesResponse.AppList.App apps);
}
