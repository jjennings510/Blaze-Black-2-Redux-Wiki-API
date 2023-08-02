package com.github.blazeblack2reduxwikiapi.dto.abilities;

import lombok.Data;

@Data
public class AbilityDto {
    Long id;
    Long pokemonId;
    String name;
    String shortEffect;
    boolean isHiddenAbility;
}
