package com.gamepricecomparator.common.service;

import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.exception.FavoriteGameNotFoundException;
import com.gamepricecomparator.common.mapper.FavoriteGameMapper;
import com.gamepricecomparator.common.repository.FavoriteGameRepository;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        if (favoriteGameRepository.findByEmailAndNameIgnoreCase(
                favoriteGameRequest.email(), favoriteGameRequest.name()).isPresent()) {
            log.info("Favorite game with name {} already exists for email {}",
                    favoriteGameRequest.name(), favoriteGameRequest.email());
            return;
        }

        favoriteGameRepository.save(favoriteGameMapper.newFavoriteGameRequestToFavoriteGame(favoriteGameRequest));
        log.info("Favorite game with name {} was successfully saved for email {}",
                favoriteGameRequest.name(), favoriteGameRequest.email());
    }

    public List<FavoriteGameResponse> getFavoriteListByEmail(String email) {
        List<FavoriteGameResponse> favoriteGames = new ArrayList<>();
        Optional<List<FavoriteGame>> favoriteGameList = favoriteGameRepository.findByEmailIgnoreCase(email);

        if (favoriteGameList.isEmpty()) return favoriteGames;

        for (FavoriteGame game : favoriteGameList.get())
            favoriteGames.add(favoriteGameMapper.favoriteGameToFavoriteGameResponse(game));

        log.debug("Was received {} games", favoriteGames.size());

        return favoriteGames;
    }

    public FavoriteGameResponse getFavoriteGameByEmailAndName(String email, String name) {
        Optional<FavoriteGame> favoriteGame = favoriteGameRepository.findByEmailAndNameIgnoreCase(email, name);

        if (favoriteGame.isEmpty())
            throw new FavoriteGameNotFoundException(HttpStatus.NOT_FOUND.value(), "Favorite game does not exist");

        return favoriteGameMapper.favoriteGameToFavoriteGameResponse(favoriteGame.get());
    }
    public void deleteFavoriteGameByEmailAndName(String email, String name) {
        favoriteGameRepository.deleteByEmailAndName(email, name);
    }
}
