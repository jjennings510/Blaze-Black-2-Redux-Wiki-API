package com.github.blazeblack2reduxwikiapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sprites")
@Data
public class Sprite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String spriteType;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id")
    @JsonIgnore
    private Pokemon pokemon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    @JsonIgnore
    private PokemonSpecies species;

    @Override
    public String toString() {
        return "Sprite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spriteType='" + spriteType + '\'' +
                '}';
    }
}
