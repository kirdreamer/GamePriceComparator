package com.gamepricecomparator.common.mapper;

import com.gamepricecomparator.common.dto.GameDTO;
import com.gamepricecomparator.common.entity.FavoriteGame;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteGameMapper {
    GameDTO favoriteGameToGameDTO(FavoriteGame favoriteGame);
}
