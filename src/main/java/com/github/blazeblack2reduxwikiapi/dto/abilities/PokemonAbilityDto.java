package com.github.blazeblack2reduxwikiapi.dto.abilities;

import com.github.blazeblack2reduxwikiapi.dto.abilities.AbilityDto;
import lombok.Data;

import java.util.List;
@Data
public class PokemonAbilityDto {
    Long pokemonId;
    List<AbilityDto> abilities;
}
