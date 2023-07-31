package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

@Data
public class LevelUpMovesDto {
    private Long id;
    private int level;
    private String move;
    private String type;
    private String category;
    private int power;
    private int accuracy;
    private String description;
}
