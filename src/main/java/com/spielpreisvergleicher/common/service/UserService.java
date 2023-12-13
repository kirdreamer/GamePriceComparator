package com.spielpreisvergleicher.common.service;

import com.spielpreisvergleicher.common.config.JwtService;
import com.spielpreisvergleicher.common.entity.user.User;
import com.spielpreisvergleicher.common.exception.UserNotFoundException;
import com.spielpreisvergleicher.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getLoggedInUser(String token) {
        String userEmail = jwtService.extractUsername(token);

        Optional<User> result = userRepository.findByEmail(userEmail);
        if (result.isEmpty()) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "User does not exist");
        }

        return result.get();
    }
}
