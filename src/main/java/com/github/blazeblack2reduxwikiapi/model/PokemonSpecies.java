package com.github.blazeblack2reduxwikiapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "pokemon_species")
@Data
public class PokemonSpecies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int number;
    private boolean isLegendary;
    private boolean isMythical;
    private boolean hasGenderDifferences;
    private String genus;
    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType .LAZY)
    @JsonManagedReference
    private List<Pokemon> varieties;
    @OneToMany(mappedBy = "species", fetch = FetchType.LAZY)
    private List<Sprite> sprite;

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", isLegendary=" + isLegendary +
                ", isMythical=" + isMythical +
                ", hasGenderDifferences=" + hasGenderDifferences +
                ", genus='" + genus + '\'' +
                '}';
    }
}
