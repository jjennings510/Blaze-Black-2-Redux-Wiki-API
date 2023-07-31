package com.github.blazeblack2reduxwikiapi.dto;

import com.github.blazeblack2reduxwikiapi.model.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PokemonDetailDto {
    Pokemon pokemon;
    String nextPokemonName;
    String previousPokemonName;
    List<String> types;
    List<AbilityDto> abilities;
    BaseStats baseStats;

    public PokemonDetailDto() {
        types = new ArrayList<>();
        abilities = new ArrayList<>();
    }

}
