package com.spielpreisvergleicher.common.mapper;

import com.spielpreisvergleicher.common.entity.FavoriteGame;
import com.spielpreisvergleicher.common.web.request.NewFavoriteGameRequest;

public interface FavoriteGameMapper {
    FavoriteGame newFavoriteGameRequestToFavoriteGame(NewFavoriteGameRequest favoriteGameRequest);
}
