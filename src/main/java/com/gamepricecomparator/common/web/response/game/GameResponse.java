package com.gamepricecomparator.common.web.response.game;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private String name;
    private String type;
    private GameInfoResponse steam;
    @Setter
    private GameInfoResponse gog;
    private String image;
    private PlatformsResponse platforms;
    private String short_description;
    private String detailed_description;
    private String about_the_game;
    @Setter
    private Boolean isFavorite;
}
