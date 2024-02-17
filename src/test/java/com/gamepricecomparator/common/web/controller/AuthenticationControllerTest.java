package com.gamepricecomparator.common.web.controller;

import com.gamepricecomparator.common.entity.user.User;
import com.gamepricecomparator.common.web.request.AuthenticationRequest;
import com.gamepricecomparator.common.web.request.RegisterRequest;
import com.gamepricecomparator.common.web.response.IsLoggedInResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthenticationControllerTest extends AbstractBaseControllerTest {

    private final static String REGISTER_URL = "/api/v1/auth/register";
    private final static String LOGIN_URL = "/api/v1/auth/login";
    private final static String LOGGED_USER_URL = "/api/v1/auth/user";

    @Nested
    @DisplayName("Registration testing")
    class TestRegister {
        @Test
        @DisplayName("(Positive) Should register successfully a new user")
        void makeSuccessfulRegister() throws Exception {
            RegisterRequest registerRequest =
                    new RegisterRequest(DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            performPostRequestWithJson(REGISTER_URL, prepareRequestAsJsonString(registerRequest))
                    .andExpect(status().isOk());

            Mockito.verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("(Negative) Should reject an attempt to register user with an existing email")
        void makeAttemptToRegisterUserWithExistingEmail() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, "testNick", DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

            RegisterRequest registerRequest =
                    new RegisterRequest(DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            performPostRequestWithJson(REGISTER_URL, prepareRequestAsJsonString(registerRequest))
                    .andExpect(status().isConflict());

            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }

        @Test
        @DisplayName("(Negative) Should reject an attempt to register user with an existing nickname")
        void makeAttemptToRegisterUserWithExistingNickname() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByNickname(DEFAULT_NICKNAME)).thenReturn(Optional.of(user));

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
        void makeSuccessfulLogin() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

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
        void makeAttemptToLoginWithNonExistingUser() throws Exception {
            AuthenticationRequest authenticationRequest =
                    new AuthenticationRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

            String loginResult = performPostRequestWithJson(LOGIN_URL, prepareRequestAsJsonString(authenticationRequest))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Mockito.verify(userRepository, times(1)).findByEmail(any(String.class));

            assertThat(objectMapper.readTree(loginResult).findValue("token")).isNull();
            assertThat(objectMapper.readTree(loginResult).findValue("message").toString())
                    .isNotNull().hasToString("\"Email does not exist\"");
        }

        @Test
        @DisplayName("(Negative) Should reject login because of wrong password")
        void makeAttemptToLoginWithWrongPassword() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

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
        void makeRequestGetLoggedInUser() throws Exception {
            User user =
                    buildMockUser(DEFAULT_ID, DEFAULT_EMAIL, DEFAULT_NICKNAME, DEFAULT_PASSWORD);

            when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

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

            String loggedInResult = performGetRequestWithToken(LOGGED_USER_URL, token)
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            IsLoggedInResponse isLoggedInResponse = objectMapper.readValue(loggedInResult, IsLoggedInResponse.class);

            assertThat(isLoggedInResponse.success()).isTrue();
        }

        @Test
        @DisplayName("(Negative) Should reject login because of non existing user")
        void makeAttemptToValidateTokenWithWrongToken() throws Exception {
            performGetRequestWithToken(LOGGED_USER_URL, wrongToken)
                    .andExpect(status().isForbidden());
        }
    }
}