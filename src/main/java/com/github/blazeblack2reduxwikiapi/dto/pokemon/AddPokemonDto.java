package com.github.blazeblack2reduxwikiapi.dto.pokemon;

import lombok.Data;

import java.util.List;

@Data
public class AddPokemonDto {
    String name;
    String speciesName;
    String formName;
    int number;
    List<String> abilities;
    List<String> types;
}
