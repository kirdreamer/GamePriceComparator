package com.spielpreisvergleicher.common.service.game.api;

public interface GameGetter {

    void getGamesByName(String name);
    void getGameById(Integer id);
}
