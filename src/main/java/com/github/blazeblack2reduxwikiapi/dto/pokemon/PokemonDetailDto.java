package com.github.blazeblack2reduxwikiapi.dto.pokemon;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PokemonDetailDto {
    Long id;
    String name;
    String formName;
    int number;
    String nextPokemonName;
    String previousPokemonName;
    List<String> types;

    public PokemonDetailDto() {
        types = new ArrayList<>();
    }

}
