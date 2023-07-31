package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddBaseStatsDto {
    private String pokemonName;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int bst;

    public AddBaseStatsDto(String pokemonName) {
        this.pokemonName = pokemonName;
    }
}
