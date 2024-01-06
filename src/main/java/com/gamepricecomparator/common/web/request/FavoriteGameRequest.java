package com.gamepricecomparator.common.web.request;

public record FavoriteGameRequest(String token, String name, Integer steamId, Integer gogId) {
}
