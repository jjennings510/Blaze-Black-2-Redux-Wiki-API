package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

@Data
public class AbilityDto {
    Long id;
    Long pokemonId;
    String name;
    String shortEffect;
    boolean isHiddenAbility;
}
