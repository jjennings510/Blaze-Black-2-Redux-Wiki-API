package com.github.blazeblack2reduxwikiapi.dto.moves;

import lombok.Data;

@Data
public class MoveDetailDto {
    Long id;
    String name;
    int power;
    int accuracy;
    int priority;
    int pp;
    int effectChance;
    int generationAdded;
    String category;
    String effect;
    String flavorText;
    String machine;
    String type;
}
