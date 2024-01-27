package com.gamepricecomparator.common.web.request;

public record FavoriteGameRequest(String name, Integer steamId, Integer gogId, String egsId) {
}
