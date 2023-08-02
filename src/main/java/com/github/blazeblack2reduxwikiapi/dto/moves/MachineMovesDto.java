package com.github.blazeblack2reduxwikiapi.dto.moves;

import lombok.Data;

@Data
public class MachineMovesDto {
    private Long id;
    private String machine;
    private String move;
    private String type;
    private String category;
    private int power;
    private int accuracy;
    private String description;
}
