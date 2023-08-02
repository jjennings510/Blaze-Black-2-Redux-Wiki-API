package com.github.blazeblack2reduxwikiapi.model.abilities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "abilities")
@Data
@NoArgsConstructor
public class Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private String name;
    private String flavorText;
    @Column(length = 2500)
    private String effect;
    private String shortEffect;
    private int generationAdded;
    @JsonBackReference
    @OneToMany(mappedBy = "ability")
    private List<PokemonAbility> pokemonSet;

    @Override
    public String toString() {
        return "Ability{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", flavorText='" + flavorText + '\'' +
                ", effect='" + effect + '\'' +
                ", shortEffect='" + shortEffect + '\'' +
                ", generationAdded=" + generationAdded +
                '}';
    }
}
