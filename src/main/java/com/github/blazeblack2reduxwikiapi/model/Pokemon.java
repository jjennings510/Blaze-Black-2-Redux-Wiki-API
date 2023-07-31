package com.github.blazeblack2reduxwikiapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "pokemon")
@Getter
@Setter
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
    private List<Sprite> sprites;
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PokemonAbility> abilities;
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<PokemonMove> moves;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "pokemon_types",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    @JsonIgnore
    private List<Type> types;
    @OneToOne(mappedBy = "pokemon", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private BaseStats baseStats;

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
