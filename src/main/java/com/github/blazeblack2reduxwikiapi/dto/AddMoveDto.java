package com.github.blazeblack2reduxwikiapi.dto;

import lombok.Data;

@Data
public class AddMoveDto {
    private String name;
    private String identifier;
    private String typeName;
    private String damageClass;
    private int pp;
    private int power;
    private int accuracy;
    private int priority;
    private String effect;
    private String machine;
    private boolean isNewMove = false;

}
