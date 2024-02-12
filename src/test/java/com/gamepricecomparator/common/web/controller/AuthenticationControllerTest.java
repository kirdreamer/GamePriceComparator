package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.web.request.AuthenticationRequest;
import com.gamepricecomparator.common.web.request.RegisterRequest;
import com.gamepricecomparator.common.web.response.IsLoggedInResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@RunWith(SpringRunner.class)
@Slf4j
@AutoConfigureMockMvc
class AuthenticationControllerTest extends AbstractBaseControllerTest {

    private final static String REGISTER_URL = "/api/v1/auth/register";
    private final static String LOGIN_URL = "/api/v1/auth/login";
    private final static String LOGGED_USER_URL = "/api/v1/auth/user";
    public static final String DEFAULT_EMAIL = "test1@example.com";
    public static final String DEFAULT_NICKNAME = "testNickname";
    public static final String DEFAULT_PASSWORD = "testPassword";
    public static final int DEFAULT_ID = 1;

    @Nested
    @DisplayName("Registration testing")
    class TestRegister {
        @Test
        @DisplayName("(Positive) Should register successfully a new user")
        void testSuccessfulRegister() throws Exception {
            RegisterRequest registerRequest =
                    new RegisterRequest(DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            performPostRequestWithJson(REGISTER_URL, prepareRequestAsJsonString(registerRequest))
                    .andExpect(status().isOk());

            Mockito.verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("(Negative) Should reject an attempt to register user with an existing email")
        void testAttemptToRegisterUserWithExistingEmail() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, "testNick", DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.ofNullable(user));

            RegisterRequest registerRequest =
                    new RegisterRequest(DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            performPostRequestWithJson(REGISTER_URL, prepareRequestAsJsonString(registerRequest))
                    .andExpect(status().isConflict());

            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }

        @Test
        @DisplayName("(Negative) Should reject an attempt to register user with an existing nickname")
        void testAttemptToRegisterUserWithExistingNickname() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByNickname(DEFAULT_NICKNAME)).thenReturn(Optional.ofNullable(user));

            RegisterRequest registerRequest =
                    new RegisterRequest("testtest@example.com", DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            performPostRequestWithJson(REGISTER_URL, prepareRequestAsJsonString(registerRequest))
                    .andExpect(status().isConflict());

            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Login testing")
    class TestLogin {
        @Test
        @DisplayName("(Positive) Should successfully login as user")
        void testSuccessfulLogin() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.ofNullable(user));

            AuthenticationRequest authenticationRequest =
                    new AuthenticationRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

            String loginResult = performPostRequestWithJson(LOGIN_URL, prepareRequestAsJsonString(authenticationRequest))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Mockito.verify(userRepository, times(2)).findByEmail(any(String.class));

            assertThat(objectMapper.readTree(loginResult).findValue("token")).isNotNull();
        }

        @Test
        @DisplayName("(Negative) Should reject login because of non existing user")
        void testAttemptToLoginWithNonExistingUser() throws Exception {
            AuthenticationRequest authenticationRequest =
                    new AuthenticationRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

            String loginResult = performPostRequestWithJson(LOGIN_URL, prepareRequestAsJsonString(authenticationRequest))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse().getContentAsString();

            Mockito.verify(userRepository, times(1)).findByEmail(any(String.class));

            assertThat(objectMapper.readTree(loginResult).findValue("token")).isNull();
            assertThat(objectMapper.readTree(loginResult).findValue("message").toString())
                    .isNotNull().hasToString("\"Email does not exist\"");
        }

        @Test
        @DisplayName("(Negative) Should reject login because of wrong password")
        void testAttemptToLoginWithWrongPassword() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.ofNullable(user));

            AuthenticationRequest authenticationRequest =
                    new AuthenticationRequest(DEFAULT_EMAIL, "ne ponyal");

            String loginResult = performPostRequestWithJson(LOGIN_URL, prepareRequestAsJsonString(authenticationRequest))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Mockito.verify(userRepository, times(2)).findByEmail(any(String.class));

            assertThat(objectMapper.readTree(loginResult).findValue("token")).isNull();
            assertThat(objectMapper.readTree(loginResult).findValue("message").toString())
                    .isNotNull().hasToString("\"Email or Password is incorrect\"");
        }
    }

    @Nested
    @DisplayName("Token validation testing")
    class TestLoggedInUser {
        @Test
        @DisplayName("(Positive) Should return response body with valid token")
        void testGetLoggedInUser() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.ofNullable(user));

            AuthenticationRequest authenticationRequest =
                    new AuthenticationRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

            String loginResult = performPostRequestWithJson(LOGIN_URL, prepareRequestAsJsonString(authenticationRequest))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Mockito.verify(userRepository, times(2)).findByEmail(any(String.class));

            String token = objectMapper.readTree(loginResult).findValue("token").toString().replaceAll("\"", "");

            assertThat(token).isNotNull();

            String loggedInResult = mvc.perform(get(LOGGED_USER_URL)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            IsLoggedInResponse isLoggedInResponse = objectMapper.readValue(loggedInResult, IsLoggedInResponse.class);

            assertThat(isLoggedInResponse.success()).isTrue();
        }

        @Test
        @DisplayName("(Negative) Should reject login because of non existing user")
        void testAttemptToValidateTokenWithWrongToken() throws Exception {
            String wrongToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsImlhdCI6MTcwNzY0OTQyOCwiZXhwIjoxNzA3NjQ5NzI4fQ.ueSCRzp4_1BzWvpNtVf36h7BaM2usDlSzD5ctJN3HybAiZ_NPXgNWspibNkVVxpx";

            mvc.perform(get(LOGGED_USER_URL)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + wrongToken))
                    .andExpect(status().isForbidden());
        }
    }
}