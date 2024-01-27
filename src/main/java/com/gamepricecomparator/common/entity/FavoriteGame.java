package com.gamepricecomparator.common.entity;

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
@Table(name = "favorite_game")
public class FavoriteGame {
    @Id
    @GeneratedValue
    private Long id;
    private Integer steamId;
    private Integer gogId;
    private String egsId;
    private String name;
    private String email;
}
