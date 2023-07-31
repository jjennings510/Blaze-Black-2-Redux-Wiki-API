package com.github.blazeblack2reduxwikiapi.dto;

import com.github.blazeblack2reduxwikiapi.model.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.Sprite;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PokemonRowDto {
    Sprite sprite;
    String pokemonName;
    String formName;
    int number;
    List<String> types;
    List<AbilityDto> abilities;
    BaseStats baseStats;

    public PokemonRowDto() {
        types = new ArrayList<>();
        abilities = new ArrayList<>();
    }
}
