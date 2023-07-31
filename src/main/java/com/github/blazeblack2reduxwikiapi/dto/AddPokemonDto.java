package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddPokemonDto {
    private String formName;
    private int number;
    private List<String> abilityNames;
    private List<String> typeNames;
}
