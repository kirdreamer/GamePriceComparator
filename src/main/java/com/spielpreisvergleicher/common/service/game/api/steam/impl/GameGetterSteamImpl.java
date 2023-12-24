package com.spielpreisvergleicher.common.service.game.api.steam.impl;

import com.spielpreisvergleicher.common.service.game.api.GameGetter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("steamGetter")
@Primary
public class GameGetterSteamImpl implements GameGetter {

    @Override
    public void getGamesByName(String name) {

    }

    @Override
    public void getGameById(Integer id) {

    }
}
