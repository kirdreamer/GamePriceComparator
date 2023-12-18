package com.spielpreisvergleicher.common.web.response.game;

public record PlatformsResponse(
        Boolean windows,
        Boolean linux,
        Boolean mac
) {
}
