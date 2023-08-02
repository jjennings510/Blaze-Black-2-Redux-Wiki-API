package com.github.blazeblack2reduxwikiapi.dto.pokemon;

import com.github.blazeblack2reduxwikiapi.dto.abilities.AbilityDto;
import com.github.blazeblack2reduxwikiapi.model.pokemon.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
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
