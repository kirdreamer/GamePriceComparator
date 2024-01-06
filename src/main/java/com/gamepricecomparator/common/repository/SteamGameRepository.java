package com.gamepricecomparator.common.repository;

import com.gamepricecomparator.common.entity.steam.SteamGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SteamGameRepository extends JpaRepository<SteamGame, Integer> {

    Optional<SteamGame> findByAppid(Integer appid);

    Optional<List<SteamGame>> findByNameIgnoreCaseContaining(String name);
}
