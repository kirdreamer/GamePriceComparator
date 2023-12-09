package com.spielpreisvergleicher.common.repository;

import com.spielpreisvergleicher.common.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
