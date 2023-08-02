package com.github.blazeblack2reduxwikiapi.service.locations;

import com.github.blazeblack2reduxwikiapi.model.locations.PokemonEncounter;
import com.github.blazeblack2reduxwikiapi.repository.locations.PokemonEncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PokemonEncounterService {

    private PokemonEncounterRepository pokemonEncounterRepository;
    @Autowired
    public PokemonEncounterService(PokemonEncounterRepository pokemonEncounterRepository) {
        this.pokemonEncounterRepository = pokemonEncounterRepository;
    }

    public void addPokemonEncounter(PokemonEncounter pokemonEncounter) {

        pokemonEncounterRepository.saveAndFlush(pokemonEncounter);
        System.out.println(pokemonEncounter);
    }
}
