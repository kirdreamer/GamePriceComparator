package com.gamepricecomparator.common.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class GameControllerTest extends AbstractBaseControllerTest {
    private final static String GET_GAME_LIST_URL = "/api/v1/games/search?name=Cyberpunk 2077";

    @BeforeAll
    static void setup() {
        setupMockStores();
    }

    @AfterAll
    static void teardown() {
        shutdownMockStores();
    }


    @Test
    @DisplayName("(Positive) Should return list of 'Cyberpunk'-games, " +
            "where response has 'Cyberpunk 2077' in Steam, Gog and Egs, " +
            "game is not favorite without token")
    void requestGetGameListWithSteamGogEgsWithoutToken() throws Exception {
        addAllGamesSteamInDatabaseMock();

        String result = performGetRequestWithoutToken(GET_GAME_LIST_URL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GameResponse> gameResponses = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(gameResponses).isNotNull().isNotEmpty();
        assertThat(gameResponses.getFirst().game_providers).hasSize(3);
        assertThat(gameResponses.getFirst().getIs_favorite()).isFalse();
    }

    @Test
    @DisplayName("(Positive) Should return list of 'Cyberpunk'-games, " +
            "where response has 'Cyberpunk 2077' in Steam, Gog and Egs, " +
            "game is not favorite with token")
    void requestGetGameListWithSteamGogEgsNotFavoriteWithToken() throws Exception {
        addAllGamesSteamInDatabaseMock();

        String result = performGetRequestWithToken(GET_GAME_LIST_URL, generateToken())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GameResponse> gameResponses = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(gameResponses).isNotNull().isNotEmpty();
        assertThat(gameResponses.getFirst().game_providers).hasSize(3);
        assertThat(gameResponses.getFirst().getIs_favorite()).isFalse();
    }


    @Test
    @DisplayName("(Positive) Should return list of 'Cyberpunk'-games, " +
            "where response has 'Cyberpunk 2077' in Gog and Egs, " +
            "game is not favorite without token")
    void requestGetGameListWithGogEgs() throws Exception {
        String result = performGetRequestWithoutToken(GET_GAME_LIST_URL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GameResponse> gameResponses = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(gameResponses).isNotNull().isNotEmpty();
        assertThat(gameResponses.getFirst().game_providers).hasSize(2);
        assertThat(gameResponses.getFirst().getIs_favorite()).isFalse();
    }

    @Test
    @DisplayName("(Positive) Should return list of 'Cyberpunk'-games, " +
            "where response has 'Cyberpunk 2077' in Steam, Gog and Egs, " +
            "game is favorite with token")
    void requestGetGameListWithSteamGogEgsFavoriteWithToken() throws Exception {
        prepareFavoriteGameCyberpunkResponse();
        addAllGamesSteamInDatabaseMock();

        String result = performGetRequestWithToken(GET_GAME_LIST_URL, generateToken())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(favoriteGameRepository, times(1)).findByEmailIgnoreCase(any(String.class));

        List<GameResponse> gameResponses = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(gameResponses).isNotNull().isNotEmpty();
        assertThat(gameResponses.getFirst().game_providers).hasSize(3);
        assertThat(gameResponses.getFirst().getIs_favorite()).isTrue();
    }
}