package com.github.blazeblack2reduxwikiapi.dto;

import com.github.blazeblack2reduxwikiapi.model.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.Sprite;
import lombok.Data;

import java.util.List;

@Data
public class MoveDetailPokemonDto {
    private Sprite sprite;
    String name;
    String formName;
    int number;
    List<String> types;
    String machine;
    String method;
    int level;
    BaseStats baseStats;
}
