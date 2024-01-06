package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.web.response.FavoriteGameResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteGameMapper {
    FavoriteGameResponse favoriteGameToFavoriteGameResponse(FavoriteGame favoriteGame);
}
