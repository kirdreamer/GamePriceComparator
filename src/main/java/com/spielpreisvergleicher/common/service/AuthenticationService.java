package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.config.JwtService;
import com.spielpreisvergleicher.common.dto.LoginResult;
import com.spielpreisvergleicher.common.entity.user.Role;
import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.exception.IncorrectCredentialsException;
import com.spielpreisvergleicher.common.exception.UserExistsException;
import com.spielpreisvergleicher.common.exception.UserNotFoundException;
import com.spielpreisvergleicher.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(String email, String nickname, String password) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserExistsException(HttpStatus.CONFLICT.value(), "User with the same Email already exists");
        if (userRepository.findByNickname(nickname).isPresent())
            throw new UserExistsException(HttpStatus.CONFLICT.value(), "User with the same Nickname already exists");

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public LoginResult authenticate(String email, String password) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isEmpty()) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "Email does not exist");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException(HttpStatus.BAD_REQUEST.value(), "Email or Password is incorrect");
        }
        User user = result.get();

        return new LoginResult(jwtService.generateToken(user), user.getNickname());
    }
}
