package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.common.config.JwtService;
import com.gamepricecomparator.common.dto.GameDTO;
import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.exception.IncorrectTokenException;
import com.gamepricecomparator.common.mapper.FavoriteGameMapper;
import com.gamepricecomparator.common.repository.FavoriteGameRepository;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteGameService {
    private final FavoriteGameRepository favoriteGameRepository;
    private final FavoriteGameMapper favoriteGameMapper;
    private final JwtService jwtService;

    public void saveFavoriteGameByEmail(String token, FavoriteGameRequest favoriteGameRequest) {
        if (Objects.isNull(token))
            throw new IncorrectTokenException(401, "Token wasn't found");

        FavoriteGame favoriteGame = FavoriteGame.builder()
                .email(jwtService.extractUsername(token))
                .name(favoriteGameRequest.name())
                .steamId(favoriteGameRequest.steamId())
                .gogId(favoriteGameRequest.gogId())
                .egsId(favoriteGameRequest.egsId())
                .build();

        if (favoriteGameRepository.findByEmailAndNameIgnoreCase(
                favoriteGame.getEmail(), favoriteGame.getName()).isPresent()) {
            log.warn("Favorite game with name {} already exists for email {}",
                    favoriteGame.getName(), favoriteGame.getEmail());
            return;
        }

        log.debug("Trying to add game with name {} for user {}...", favoriteGame.getName(), favoriteGame.getEmail());
        favoriteGameRepository.save(favoriteGame);
        log.debug("Favorite game with name {} was successfully saved for email {}",
                favoriteGame.getName(), favoriteGame.getEmail());
    }

    public Map<String, GameDTO> getFavoriteGamesByEmail(String token) {
        Map<String, GameDTO> favoriteGameDTOS = new HashMap<>();
        Optional<List<FavoriteGame>> favoriteGameList = favoriteGameRepository.findByEmailIgnoreCase(
                jwtService.extractUsername(token)
        );

        if (favoriteGameList.isEmpty()) return favoriteGameDTOS;

        for (FavoriteGame game : favoriteGameList.get())
            favoriteGameDTOS.put(game.getName(), favoriteGameMapper.favoriteGameToGameDTO(game));


        log.debug("Were received {} games", favoriteGameDTOS.size());

        return favoriteGameDTOS;
    }

    public void deleteFavoriteGameByEmailAndName(String token, String name) {
        String user = jwtService.extractUsername(token);
        log.debug("Trying to delete favorite game {} for user {}...", name, user);
        favoriteGameRepository.deleteByEmailAndName(user, name);
        log.debug("Deleting was successfully completed");
    }

    public List<String> getAllEmailsByGame(String gameName) {
        log.debug("Trying to extract all email in favorite games by name {} from Database...", gameName);
        List<String> emails = favoriteGameRepository.findEmailsByName(gameName).orElse(new ArrayList<>());
        log.debug("Extracting was successfully completed");

        return emails;
    }

    public List<GameDTO> getAllFavoriteGames() {
        log.debug("Trying to extract all favorite games from Database...");
        List<GameDTO> favoriteGameInfoDTOS = favoriteGameRepository.findAllFavoriteGames()
                .orElse(new ArrayList<>());
        log.debug("Extracting was successfully completed");

        return favoriteGameInfoDTOS;
    }

    public void setAllFavoriteGamesInListAsFavoriteGame(List<GameResponse> games, String token) {
        log.debug("Trying to set all Favorite games for current in Game list as favorite game...");

        Map<String, GameDTO> favoriteGames = getFavoriteGamesByEmail(token);

        if (!favoriteGames.isEmpty())
            for (GameResponse game : games)
                if (favoriteGames.containsKey(game.getName()))
                    game.setIs_favorite(true);

        log.debug("All Favorite games were set as favorite game");
    }

    public void setAllGamesInListAsFavoriteGame(List<GameResponse> games) {
        log.debug("Setting all games in list as favorite game...");
        for (GameResponse game : games) game.setIs_favorite(true);
        log.debug("All games were set as favorite game");
    }
}
