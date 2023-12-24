package com.spielpreisvergleicher.common.dto;

import java.util.List;

public record SteamAllGamesResponse(AppList applist) {
    public record AppList(List<Apps> apps) {
        public record Apps(Integer appid, String name) {}
    }
}
