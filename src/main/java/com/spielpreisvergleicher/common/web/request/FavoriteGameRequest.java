package com.spielpreisvergleicher.common.web.request;

public record FavoriteGameRequest(String email, String name, Integer steamId, Integer gogId) {
}
