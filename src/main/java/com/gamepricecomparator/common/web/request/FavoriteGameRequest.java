package com.gamepricecomparator.common.web.request;

public record FavoriteGameRequest(String email, String name, Integer steamId, Integer gogId) {
}
