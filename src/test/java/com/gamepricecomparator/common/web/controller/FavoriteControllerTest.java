package com.gamepricecomparator.common.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.web.request.FavoriteGameRequest;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class FavoriteControllerTest extends AbstractBaseControllerTest {

    private final static String ADD_FAVORITE_URL = "/api/v1/favorite/add";
    private final static String GET_FAVORITE_LIST_URL = "/api/v1/favorite/get-list";
    private final static String DELETE_FAVORITE_URL = "/api/v1/favorite/delete-game";

    @Nested
    @DisplayName("Add Favorite Game To User")
    class TestAddFavoriteGameToUser {
        FavoriteGameRequest prepareFavoriteGameCyberpunkRequest() {
            return new FavoriteGameRequest("Cyberpunk 2077", 1091500, 2093619782, "70b2be5bbcbd4d90a3f209b8e498255f");
        }

        @Test
        @DisplayName("(Positive) Should add game as favorite for current user")
        void addGameAsFavoriteToUser() throws Exception {
            performPostRequestWithJsonAndToken(
                    ADD_FAVORITE_URL,
                    prepareRequestAsJsonString(prepareFavoriteGameCyberpunkRequest()),
                    generateToken())
                    .andExpect(status().isOk());

            Mockito.verify(favoriteGameRepository, times(1)).save(any(FavoriteGame.class));
        }

        @Test
        @DisplayName("(Negative) Should reject request 'add game as favorite' without token")
        void rejectGameAsFavoriteToUserWithoutToken() throws Exception {
            performPostRequestWithJson(ADD_FAVORITE_URL, prepareRequestAsJsonString(prepareFavoriteGameCyberpunkRequest()))
                    .andExpect(status().isBadRequest());

            Mockito.verify(favoriteGameRepository, times(0)).save(any(FavoriteGame.class));
        }

        @Test
        @DisplayName("(Negative) Should reject request 'add game as favorite' with wrong token")
        void rejectGameAsFavoriteToUserWithWrongToken() throws Exception {
            performPostRequestWithJsonAndToken(
                    ADD_FAVORITE_URL, prepareRequestAsJsonString(prepareFavoriteGameCyberpunkRequest()), wrongToken)
                    .andExpect(status().isForbidden());

            Mockito.verify(favoriteGameRepository, times(0)).save(any(FavoriteGame.class));
        }
    }

    @Nested
    @DisplayName("Get Favorite List By Email")
    class TestGetFavoriteListByEmail {

        @Test
        @DisplayName("(Positive) Should return the list of favorite games by email")
        void returnGetFavoriteListByEmail() throws Exception {
            setupMockStores();
            prepareFavoriteGameCyberpunkResponse();

            String favoriteListResult = performGetRequestWithToken(GET_FAVORITE_LIST_URL, generateToken())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Mockito.verify(favoriteGameRepository, times(1))
                    .findByEmailIgnoreCase(any(String.class));

            List<GameResponse> gameResponses = objectMapper.readValue(favoriteListResult, new TypeReference<>() {
            });

            assertThat(gameResponses).isNotNull().isNotEmpty();
            assertThat(gameResponses.getFirst().game_providers).hasSize(3);
            assertThat(gameResponses.getFirst().getIs_favorite()).isTrue();
        }

        @Test
        @DisplayName("(Negative) Should reject request to get the list of favorite games without token")
        void rejectAttemptGetFavoriteListByEmail() throws Exception {

            performGetRequestWithoutToken(GET_FAVORITE_LIST_URL)
                    .andExpect(status().isBadRequest());

            Mockito.verify(favoriteGameRepository, times(0))
                    .findByEmailIgnoreCase(any(String.class));
        }

    }

    @Nested
    @DisplayName("Delete Favorite Game By Name")
    class TestDeleteFavoriteGameByName {
        @Test
        @DisplayName("(Positive) Should delete game by name and by email")
        void deleteFavoriteGameByName() {
            try {
                mvc.perform(delete(DELETE_FAVORITE_URL)
                                .param("name", "Cyberpunk 2077")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken()))
                        .andExpect(status().isAccepted());
            } catch (Exception e) {
                log.error("an error occurred: {}", e.getMessage());
                Assertions.fail();
            }

            Mockito.verify(favoriteGameRepository, times(1))
                    .deleteByEmailAndName(any(String.class), any(String.class));

        }

        @Test
        @DisplayName("(Negative) Should reject to delete game by email without token")
        void rejectDeleteFavoriteGameByNameWithoutToken() {
            try {
                mvc.perform(delete(DELETE_FAVORITE_URL)
                                .param("name", "Cyberpunk 2077"))
                        .andExpect(status().isBadRequest());
            } catch (Exception e) {
                log.error("an error occurred: {}", e.getMessage());
                Assertions.fail();
            }

            Mockito.verify(favoriteGameRepository, times(0))
                    .deleteByEmailAndName(any(String.class), any(String.class));

        }

        @Test
        @DisplayName("(Negative) Should reject to delete game by email without name of the game")
        void rejectDeleteFavoriteGameWithoutName() {
            try {
                mvc.perform(delete(DELETE_FAVORITE_URL)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken()))
                        .andExpect(status().isBadRequest());
            } catch (Exception e) {
                log.error("an error occurred: {}", e.getMessage());
                Assertions.fail();
            }

            Mockito.verify(favoriteGameRepository, times(0))
                    .deleteByEmailAndName(any(String.class), any(String.class));

        }
    }

    @Test
    void sendPriceAlarmEmails() {
    }
}