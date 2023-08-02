package com.github.blazeblack2reduxwikiapi.model.encounters;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.blazeblack2reduxwikiapi.model.locations.PokemonEncounter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "encounter_methods")
public class EncounterMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    int number;
    @OneToMany(mappedBy = "method", orphanRemoval = true)
    @JsonManagedReference
    List<PokemonEncounter> encounters;

    @Override
    public String toString() {
        return "EncounterMethod{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
