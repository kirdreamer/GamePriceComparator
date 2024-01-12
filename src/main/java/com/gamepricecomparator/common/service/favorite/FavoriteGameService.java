package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.common.config.JwtService;
import com.gamepricecomparator.common.dto.projection.FavoriteGameInfoDTO;
import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.exception.FavoriteGameNotFoundException;
import com.gamepricecomparator.common.exception.IncorrectTokenException;
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

    public List<FavoriteGameResponse> getFavoriteListByEmail(String token) {
        List<FavoriteGameResponse> favoriteGames = new ArrayList<>();
        Optional<List<FavoriteGame>> favoriteGameList = favoriteGameRepository.findByEmailIgnoreCase(
                jwtService.extractUsername(token)
        );

        if (favoriteGameList.isEmpty()) return favoriteGames;

        for (FavoriteGame game : favoriteGameList.get())
            favoriteGames.add(favoriteGameMapper.favoriteGameToFavoriteGameResponse(game));

        log.debug("Was received {} games", favoriteGames.size());

        return favoriteGames;
    }

    public FavoriteGameResponse getFavoriteGameByEmailAndName(String token, String name) {
        Optional<FavoriteGame> favoriteGame = favoriteGameRepository.findByEmailAndNameIgnoreCase(
                jwtService.extractUsername(token), name);

        if (favoriteGame.isEmpty())
            throw new FavoriteGameNotFoundException(HttpStatus.NOT_FOUND.value(), "Favorite game does not exist");

        return favoriteGameMapper.favoriteGameToFavoriteGameResponse(favoriteGame.get());
    }

    public void deleteFavoriteGameByEmailAndName(String token, String name) {
        favoriteGameRepository.deleteByEmailAndName(jwtService.extractUsername(token), name);
    }

    public String[] getAllEmailsByGame(String gameName) {
        log.debug("Trying to extract all email in favorite games by name {} from Database...", gameName);
        String[] emails = favoriteGameRepository.findEmailsByName(gameName)
                .orElse(new ArrayList<>())
                .toArray(String[]::new);
        log.debug("Extracting was successfully completed");

        return emails;
    }

    public List<FavoriteGameInfoDTO> getAllFavoriteGames() {
        log.debug("Trying to extract all favorite games from Database...");
        List<FavoriteGameInfoDTO> favoriteGameInfoDTOS = favoriteGameRepository.findAllFavoriteGames()
                .orElse(new ArrayList<>());
        log.debug("Extracting was successfully completed");

        return favoriteGameInfoDTOS;
    }
}
