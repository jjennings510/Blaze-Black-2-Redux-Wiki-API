package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

import java.util.List;
@Data
public class PokemonAbilityDto {
    Long pokemonId;
    List<AbilityDto> abilities;
}
