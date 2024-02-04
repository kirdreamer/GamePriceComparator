package com.gamepricecomparator.common.web.response.game;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private String name;
    private String type;
    public List<GameProviderResponse> game_providers;
    private String image;
    private PlatformsResponse platforms;
    private String short_description;
    private String detailed_description;
    private String about_the_game;
    @Setter
    private Boolean is_favorite;
}
