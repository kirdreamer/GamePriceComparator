package com.spielpreisvergleicher.common.web.request;

public record NewFavoriteGameRequest(String email, String name, Integer steamId, Integer gogId) {
}
