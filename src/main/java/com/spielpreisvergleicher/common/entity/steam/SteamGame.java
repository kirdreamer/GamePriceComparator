package com.spielpreisvergleicher.common.entity.steam;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "steam_game")
public class SteamGame {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer appid;
    private String name;
}
