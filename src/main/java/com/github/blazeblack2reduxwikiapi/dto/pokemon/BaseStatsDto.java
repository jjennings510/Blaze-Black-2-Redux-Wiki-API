package com.github.blazeblack2reduxwikiapi.dto.pokemon;

import lombok.Data;

@Data
public class BaseStatsDto {
    private Long id;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int bst;
}
