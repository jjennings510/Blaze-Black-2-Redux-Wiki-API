package com.github.blazeblack2reduxwikiapi.dto.moves;

import lombok.Data;

@Data
public class AddPokemonMoveDto {
    String pokemonName;
    String moveName;
    int level;
    String method;
}
