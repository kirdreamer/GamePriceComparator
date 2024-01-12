package com.gamepricecomparator.common.repository;

import com.gamepricecomparator.common.entity.FavoriteGame;
import com.gamepricecomparator.common.dto.favorite.projection.FavoriteGameInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteGameRepository extends JpaRepository<FavoriteGame, Long> {
    Optional<List<FavoriteGame>> findByEmailIgnoreCase(String email);

    Optional<FavoriteGame> findByEmailAndNameIgnoreCase(String email, String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM FavoriteGame f WHERE f.email=:email AND f.name=:name")
    void deleteByEmailAndName(@Param("email") String email, @Param("name") String name);

    @Query("SELECT DISTINCT " +
            "new com.gamepricecomparator.common.dto.favorite.projection.FavoriteGameInfoDTO(f.name, f.gogId, f.steamId)" +
            " FROM FavoriteGame f GROUP BY f.name, f.gogId, f.steamId")
    Optional<List<FavoriteGameInfoDTO>> findAllFavoriteGames();

    @Query ("SELECT DISTINCT f.email FROM FavoriteGame f WHERE f.name=:name")
    Optional<List<String>> findEmailsByName(@Param("name") String name);


}

