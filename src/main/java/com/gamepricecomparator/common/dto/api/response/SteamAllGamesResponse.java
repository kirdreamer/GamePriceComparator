package com.gamepricecomparator.common.dto.api.response;

import java.util.List;

public record SteamAllGamesResponse(AppList applist) {
    public record AppList(List<App> apps) {
        public record App(Integer appid, String name) {}
    }
}
