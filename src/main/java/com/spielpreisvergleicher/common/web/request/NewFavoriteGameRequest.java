package com.spielpreisvergleicher.common.web.request;

public record NewFavoriteGameRequest(String email, FavoriteGame favoriteGame) {
    public record FavoriteGame(String name, Integer steamId, Integer gogId) {}
}
