package com.spielpreisvergleicher.common.repository;

import com.spielpreisvergleicher.common.entity.FavoriteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteGameRepository extends JpaRepository<FavoriteGame, Long> {
    Optional<List<FavoriteGame>> findByEmailIgnoreCaseContaining(String email);
}
