package com.spielpreisvergleicher.common.mapper;

import com.spielpreisvergleicher.common.entity.FavoriteGame;
import com.spielpreisvergleicher.common.web.request.FavoriteGameRequest;
import com.spielpreisvergleicher.common.web.response.FavoriteGameResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteGameMapper {
    FavoriteGame newFavoriteGameRequestToFavoriteGame(FavoriteGameRequest favoriteGameRequest);
    FavoriteGameResponse favoriteGameToFavoriteGameResponse(FavoriteGame favoriteGame);
}
