package com.github.blazeblack2reduxwikiapi.model.pokemon;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.blazeblack2reduxwikiapi.model.Sprite;
import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import com.github.blazeblack2reduxwikiapi.model.locations.PokemonEncounter;
import com.github.blazeblack2reduxwikiapi.model.moves.PokemonMove;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pokemon")
@Data
@NoArgsConstructor
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String speciesName;
    private String formName;
    private int number;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    @JsonBackReference
    private PokemonSpecies species;
    @OneToMany(mappedBy = "pokemon", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Sprite> sprites;
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PokemonAbility> abilities;
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PokemonMove> moves;

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PokemonEncounter> encounters;

    @OneToMany(mappedBy = "pokemon", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @JsonManagedReference
    private List<PokemonType> types;

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + speciesName + '\'' +
                ", formName='" + formName + '\'' +
                ", number=" + number +
                '}';
    }
}
