package com.gamepricecomparator.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "favorite_game")
@Data
public class FavoriteGame {
    @Id
    @GeneratedValue
    public Long id;
    public Integer steamId;
    public Integer gogId;
    public String name;
    public String email;
}
