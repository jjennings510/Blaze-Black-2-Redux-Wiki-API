package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

@Data
public class MoveRowDto {
    Long id;
    String name;
    String effect;
    String type;
    String category;
    int power;
    int accuracy;
}
