package com.github.blazeblack2reduxwikiapi.dto.locations;

import lombok.Data;

@Data
public class AddPokemonEncounterDto {
    String pokemonName;
    String locationAreaName;
    int chance;
    int minLevel;
    int maxLevel;
    String method;
    String condition;
}
