package com.gamepricecomparator.common.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gamepricecomparator.StartTestApplication;
import com.gamepricecomparator.common.entity.user.Role;
import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("integration-test")
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = StartTestApplication.class)
public abstract class AbstractBaseControllerTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1");

    @MockBean
    protected UserRepository userRepository;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected String prepareRequestAsJsonString(Object request) {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String jsonRequest = null;
        try {
            jsonRequest = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("an error occurred: {}", e.getMessage());
            Assert.fail();
        }
        assertThat(jsonRequest).isNotNull();
        return jsonRequest;
    }

    @BeforeAll
    public static void setupContainer() {
        log.info("Trying to start test postgreSQL...");
        postgres.start();
        log.info("Test PostgreSQL was started");
    }

    @AfterAll
    public static void shutdown() {
        log.info("Trying to stop test postgreSQL...");
        postgres.stop();
        log.info("Test PostgreSQL was stoped");
    }

    protected User buildMockUser(Integer id, String email, String nickname, String password) {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
    }

    protected ResultActions performPostRequestWithJson(String url, String jsonRequest) throws Exception {
        try {
        return mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        } catch (Exception e) {
            log.error("an error occurred: {}", e.getMessage());
            Assert.fail();
        }
        return null;
    }
}
