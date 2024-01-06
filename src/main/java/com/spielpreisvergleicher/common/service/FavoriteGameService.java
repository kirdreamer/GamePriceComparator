package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.mapper.FavoriteGameMapper;
import com.spielpreisvergleicher.common.repository.FavoriteGameRepository;
import com.spielpreisvergleicher.common.web.request.FavoriteGameRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteGameService {
    private final FavoriteGameRepository favoriteGameRepository;
    private final FavoriteGameMapper favoriteGameMapper;

    public void saveFavoriteGameByEmail(FavoriteGameRequest favoriteGameRequest) {
        favoriteGameRepository.save(favoriteGameMapper.newFavoriteGameRequestToFavoriteGame(favoriteGameRequest));
        log.info("Favorite game with name {} was successfully saved for email {}",
                favoriteGameRequest.name(), favoriteGameRequest.email());
    }
}
