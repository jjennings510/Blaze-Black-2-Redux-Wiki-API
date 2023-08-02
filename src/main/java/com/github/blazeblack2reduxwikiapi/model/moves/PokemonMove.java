package com.github.blazeblack2reduxwikiapi.model.moves;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pokemon_moves")
@Data
public class PokemonMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id")
    @JsonManagedReference
    private Pokemon pokemon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "move_id")
    private Move move;
    private int level;
    private String method;

    @Override
    public String toString() {
        return "PokemonMove{" +
                "id=" + id +
                ", level=" + level +
                ", method='" + method + '\'' +
                '}';
    }
}
