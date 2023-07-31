package com.github.blazeblack2reduxwikiapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pokemon_abilities")
@Getter
@Setter
@NoArgsConstructor
public class PokemonAbility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id")
    @JsonManagedReference
    private Pokemon pokemon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id")
    @JsonManagedReference
    private Ability ability;
    private boolean isHiddenAbility;
    private int number;

    @Override
    public String toString() {
        return "PokemonAbility{" +
                "id=" + id +
                ", isHiddenAbility=" + isHiddenAbility +
                '}';
    }

    public PokemonAbility(Pokemon pokemon, Ability ability, boolean isHiddenAbility) {
        this.pokemon = pokemon;
        this.ability = ability;
        this.isHiddenAbility = isHiddenAbility;
    }
    public void removePokemon(Pokemon pokemon) {
        pokemon.getAbilities().remove(this);
        this.pokemon = null;
    }
}
