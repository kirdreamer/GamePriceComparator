package com.gamepricecomparator.common.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gamepricecomparator.StartTestApplication;
import com.gamepricecomparator.common.dto.GameDTO;
import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.entity.steam.SteamGame;
import com.gamepricecomparator.common.entity.user.Role;
import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.repository.FavoriteGameRepository;
import com.gamepricecomparator.common.repository.SteamGameRepository;
import com.gamepricecomparator.common.repository.UserRepository;
import com.gamepricecomparator.common.service.AuthenticationService;
import com.gamepricecomparator.common.service.favorite.FavoriteListEmailSenderService;
import com.gamepricecomparator.common.service.game.api.steam.impl.SteamService;
import com.gamepricecomparator.common.web.request.AuthenticationRequest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("integration-test")
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = StartTestApplication.class)
public abstract class AbstractBaseControllerTest {
    private static final String JSON_FOLDER_PATH = "jsonResponses/";
    protected String wrongToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsImlhdCI6MTcxNzk2MTE2MiwiZXhwIjoxNzE3OTYxNDYyfQ.q_d_edgPnqdx6rcM2fi9abwyWvZG0NrfR07kgvKEtAvxbE_4hmKcHerzEnPMEnGW";
    protected static final String DEFAULT_EMAIL = "test1@example.com";
    protected static final String DEFAULT_NICKNAME = "testNickname";
    protected static final String DEFAULT_PASSWORD = "testPassword";
    protected static final int DEFAULT_ID = 1;
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16.1");
    protected static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(options().port(8080));

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected SteamGameRepository steamGameRepository;

    @MockBean
    protected FavoriteGameRepository favoriteGameRepository;

    @SpyBean
    protected FavoriteListEmailSenderService favoriteListEmailSenderService;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected AuthenticationService authenticationService;

    @Autowired
    protected SteamService steamService;

    protected String generateToken() {
        Mockito.when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.ofNullable(
                buildMockUser(DEFAULT_NICKNAME)
        ));

        AuthenticationRequest request = new AuthenticationRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        return authenticationService.authenticate(request.email(), request.password()).token();
    }

    protected void addAllGamesSteamInDatabaseMock() {
        addAllSteamGamesStub();
        steamService.getAllSteamGamesAndSaveIntoDatabase();

        Mockito.when(steamGameRepository.findByNameIgnoreCaseContaining(any(String.class)))
                .thenReturn(Optional.of(List.of(new SteamGame(1091500, "Cyberpunk 2077"))));
    }

    protected String prepareRequestAsJsonString(Object request) {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String jsonRequest = null;
        try {
            jsonRequest = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("an error occurred: {}", e.getMessage());
            Assertions.fail();
        }
        assertThat(jsonRequest).isNotNull();
        return jsonRequest;
    }

    @BeforeAll
    public static void setupContainer() {
        log.info("Trying to start test postgreSQL...");
        POSTGRES.start();
        log.info("Test PostgreSQL was started");
    }

    @AfterAll
    public static void shutdown() {
        log.info("Trying to stop test postgreSQL...");
        POSTGRES.stop();
        log.info("Test PostgreSQL was stoped");
    }

    protected User buildMockUser(String nickname) {
        return User.builder()
                .id(DEFAULT_ID)
                .email(DEFAULT_EMAIL)
                .nickname(nickname)
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .role(Role.USER)
                .build();
    }

    protected FavoriteGame buildMockFavoriteGameCyberpunk() {
        return new FavoriteGame(
                1L,
                1091500,
                2093619782,
                "70b2be5bbcbd4d90a3f209b8e498255f",
                "Cyberpunk 2077",
                DEFAULT_EMAIL);
    }

    protected void prepareFavoriteGameCyberpunkResponse() {
        FavoriteGame favoriteGame = buildMockFavoriteGameCyberpunk();

        GameDTO gameDTO = new GameDTO(
                favoriteGame.getName(),
                favoriteGame.getSteamId(),
                favoriteGame.getGogId(),
                favoriteGame.getEgsId());

        Mockito.when(favoriteGameRepository.findByEmailAndNameIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(Optional.of(favoriteGame));

        Mockito.when(favoriteGameRepository.findByEmailIgnoreCase(any(String.class)))
                .thenReturn(Optional.of(List.of(favoriteGame)));

        Mockito.when(favoriteGameRepository.findEmailsByName(any(String.class)))
                .thenReturn(Optional.of(List.of(DEFAULT_EMAIL)));

        Mockito.when(favoriteGameRepository.findAllFavoriteGames())
                .thenReturn(Optional.of(List.of(gameDTO)));
    }

    protected ResultActions performPostRequestWithJson(String url, String jsonRequest) {
        try {
            return mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest));
        } catch (Exception e) {
            log.error("an error occurred: {}", e.getMessage());
            Assertions.fail();
        }
        return null;
    }

    protected ResultActions performPostRequestWithJsonAndToken(String url, String jsonRequest, String token) {
        try {
            return mvc.perform(post(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest));
        } catch (Exception e) {
            log.error("an error occurred: {}", e.getMessage());
            Assertions.fail();
        }
        return null;
    }

    protected ResultActions performGetRequestWithToken(String url, String token) {
        try {
            return mvc.perform(get(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        } catch (Exception e) {
            log.error("an error occurred: {}", e.getMessage());
            Assertions.fail();
        }
        return null;
    }

    protected ResultActions performGetRequestWithoutToken(String url) {
        try {
            return mvc.perform(get(url));
        } catch (Exception e) {
            log.error("an error occurred: {}", e.getMessage());
            Assertions.fail();
        }
        return null;
    }

    static void setupMockStores() {
        log.info("Trying to start wireMockServer...");
        WIRE_MOCK_SERVER.start();
        addSteamStub();
        addGogStub();
        addEgsStub();
        log.info("WireMockServer was started");
    }

    static void addAllSteamGamesStub() {
        WIRE_MOCK_SERVER.stubFor(WireMock.get(WireMock.urlEqualTo("/ISteamApps/GetAppList/v2/"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"applist\": { \"apps\": [{\"appid\": 1091500, \"name\": \"Cyberpunk 2077\"}]}}")));

        log.info("AllSteamGamesStub was added");
    }

    static void addSteamStub() {
        WIRE_MOCK_SERVER.stubFor(WireMock.get(WireMock.urlEqualTo("/api/appdetails?appids=1091500&cc=de"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile(JSON_FOLDER_PATH + "steamCyberpunkResponse.json")));

        log.info("SteamStub was added");
    }

    static void addGogStub() {
        WIRE_MOCK_SERVER.stubFor(WireMock.get(WireMock.urlEqualTo("/games/ajax/filtered?search=Cyberpunk%202077"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile(JSON_FOLDER_PATH + "gogCyberpunkResponse.json")));

        log.info("GogStub was added");
    }

    static void addEgsStub() {
        WIRE_MOCK_SERVER.stubFor(WireMock.post(WireMock.urlEqualTo("/graphql"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile(JSON_FOLDER_PATH + "egsCyberpunkResponse.json")));

        log.info("EgsStub was added");
    }

    static void shutdownMockStores() {
        log.info("Trying to stop wireMockServer...");
        WIRE_MOCK_SERVER.shutdown();
        log.info("WireMockServer was stoped");
    }
}
