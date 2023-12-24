package com.spielpreisvergleicher.common.web.response.game;

public record GameResponse(
        String name,
        String type,
        GameInfoResponse steam,
        GameInfoResponse gog,
        String image,
        PlatformsResponse platforms,
        String shortDescription,
        String detailedDescription,
        String aboutTheGame
) {
}
