package com.gamepricecomparator.common.web.response.game;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private String name;
    private String type;
    //TODO make GameInfoResponse as list
    private GameInfoResponse steam;
    @Setter
    private GameInfoResponse gog;
    @Setter
    private GameInfoResponse egs;
    private String image;
    private PlatformsResponse platforms;
    private String short_description;
    private String detailed_description;
    private String about_the_game;
    @Setter
    private Boolean isFavorite;
}
