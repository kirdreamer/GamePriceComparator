package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.entity.FavoriteGame;
import com.spielpreisvergleicher.common.mapper.FavoriteGameMapper;
import com.spielpreisvergleicher.common.repository.FavoriteGameRepository;
import com.spielpreisvergleicher.common.web.request.FavoriteGameRequest;
import com.spielpreisvergleicher.common.web.response.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<FavoriteGameResponse> getFavoriteListByEmail(String email) {
        List<FavoriteGameResponse> favoriteGames = new ArrayList<>();
        Optional<List<FavoriteGame>> favoriteGameList = favoriteGameRepository.findByEmailIgnoreCaseContaining(email);

        if (favoriteGameList.isEmpty()) return favoriteGames;

        for (FavoriteGame game : favoriteGameList.get())
            favoriteGames.add(favoriteGameMapper.favoriteGameToFavoriteGameResponse(game));

        return favoriteGames;
    }
}
