package com.github.blazeblack2reduxwikiapi.dto.moves;

import lombok.Data;

@Data
public class AddMoveDto {
    boolean isNewMove;
    String name;
    String identifier;
    int pp;
    int Power;
    int accuracy;
    int priority;
    int effectChance;
    int generationAdded;
    String damageClass;
    String effect;
    String shortEffect;
    String flavorText;
    String machine;
    String typeName;
}
