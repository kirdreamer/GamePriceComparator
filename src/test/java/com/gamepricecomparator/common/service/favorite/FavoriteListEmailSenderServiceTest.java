package com.gamepricecomparator.common.service.favorite;

import com.gamepricecomparator.StartTestApplication;
import com.gamepricecomparator.common.constant.Platform;
import com.gamepricecomparator.common.web.response.game.GameProviderResponse;
import com.gamepricecomparator.common.web.response.game.GameResponse;
import com.gamepricecomparator.common.web.response.game.PriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integration-test")
@Slf4j
@SpringBootTest(classes = StartTestApplication.class)
class FavoriteListEmailSenderServiceTest {

    private GameResponse buildMockGameResponse(Double initialPrice, Double finalPrice, Boolean isFree) {
        List<GameProviderResponse> gameProviders = List.of(
                new GameProviderResponse(
                        Platform.GOG,
                        "1",
                        new PriceResponse(initialPrice, finalPrice, null, null, isFree),
                        null
                )
        );

        return new GameResponse(
                "Cyberpunk 2077",
                "game",
                gameProviders,
                null,
                null,
                null,
                null,
                null,
                true
        );
    }

    @Nested
    @DisplayName("Check if Price low cases")
    class TestCheckIfPriceLow {
        @Autowired
        FavoriteListEmailSenderService favoriteListEmailSenderService;

        @Test
        @DisplayName("Should return true, if game is free")
        void returnTrueIfFree() {
            assertThat(favoriteListEmailSenderService.checkIfPriceLow(
                    buildMockGameResponse(1., 1., true)
            )).isTrue();
        }

        @Test
        @DisplayName("Should return true, if final price is lower than initial")
        void returnTrueIfFinalLower() {
            assertThat(favoriteListEmailSenderService.checkIfPriceLow(
                    buildMockGameResponse(2., 1., false)
            )).isTrue();
        }

        @Test
        @DisplayName("Should return false, if final price equals than initial")
        void returnFalseIfFinalEqualsInitial() {
            assertThat(favoriteListEmailSenderService.checkIfPriceLow(
                    buildMockGameResponse(2., 2., false)
            )).isFalse();
        }

    }
}