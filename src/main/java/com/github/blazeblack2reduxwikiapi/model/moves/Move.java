package com.github.blazeblack2reduxwikiapi.model.moves;

import com.github.blazeblack2reduxwikiapi.model.pokemon.Type;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
@Entity
@Table(name = "moves")
@Data
@NoArgsConstructor
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    private String identifier;
    private int power;
    private int accuracy;
    private int priority;
    private int pp;
    private int effectChance;
    private int generationAdded;
    private String damageClass;
    @Column(length = 4500)
    private String effect;
    @Column(length = 1500)
    private String shortEffect;
    @Column(length = 500)
    private String flavorText;
    private String machine;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    @OneToMany(mappedBy = "move", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PokemonMove> pokemonSet;

    @Override
    public String toString() {
        return "Move{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", power=" + power +
                ", accuracy=" + accuracy +
                ", priority=" + priority +
                ", pp=" + pp +
                ", damageClass='" + damageClass + '\'' +
                ", effect='" + effect + '\'' +
                ", shortEffect='" + shortEffect + '\'' +
                ", flavorText='" + flavorText + '\'' +
                ", machine='" + machine + '\'' +
                '}';
    }
}
