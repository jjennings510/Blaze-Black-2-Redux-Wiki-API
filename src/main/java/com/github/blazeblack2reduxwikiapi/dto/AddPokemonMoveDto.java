package com.github.blazeblack2reduxwikiapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPokemonMoveDto {
    private String pokemonName;
    private String moveName;
    private int level;
    private String method;
}
