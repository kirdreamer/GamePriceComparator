package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.mapper.FavoriteGameMapper;
import com.spielpreisvergleicher.common.repository.FavoriteGameRepository;
import com.spielpreisvergleicher.common.web.request.NewFavoriteGameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteGameService {
    private final FavoriteGameRepository favoriteGameRepository;
    private final FavoriteGameMapper favoriteGameMapper;

    public void saveFavoriteGameByEmail(NewFavoriteGameRequest favoriteGameRequest) {
        favoriteGameRepository.save(favoriteGameMapper.newFavoriteGameRequestToFavoriteGame(favoriteGameRequest));
    }
}
