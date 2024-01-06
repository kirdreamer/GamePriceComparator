package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.FavoriteGameResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteGameMapper {
    FavoriteGame newFavoriteGameRequestToFavoriteGame(FavoriteGameRequest favoriteGameRequest);
    FavoriteGameResponse favoriteGameToFavoriteGameResponse(FavoriteGame favoriteGame);
}
