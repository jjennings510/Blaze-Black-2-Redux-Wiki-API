package com.github.blazeblack2reduxwikiapi.model.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterConditionValue;
import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterMethod;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pokemon_encounters")
public class PokemonEncounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int chance;
    int maxLevel;
    int minLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id")
    @JsonManagedReference
    Pokemon pokemon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_area_id")
    @JsonBackReference
    LocationArea locationArea;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id")
    EncounterMethod method;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    EncounterConditionValue condition;

}
