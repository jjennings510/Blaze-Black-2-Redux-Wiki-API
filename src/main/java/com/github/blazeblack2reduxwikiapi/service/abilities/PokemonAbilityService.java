package com.github.blazeblack2reduxwikiapi.service.abilities;

import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import com.github.blazeblack2reduxwikiapi.repository.abilities.PokemonAbilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PokemonAbilityService {
    private PokemonAbilityRepository pokemonAbilityRepository;
    @Autowired
    public PokemonAbilityService(PokemonAbilityRepository pokemonAbilityRepository) {
        this.pokemonAbilityRepository = pokemonAbilityRepository;
    }
    public List<PokemonAbility> getAbilitiesForPokemonIds(Long[] ids) {
        return pokemonAbilityRepository.findByPokemonIds(ids);
    }
//    public List<PokemonAbility> getAbilitiesForPokemonId(Long id) {
//        return pokemonAbilityRepository.findByPokemonId(id);
//    }
    public CompletableFuture<List<PokemonAbility>> getAbilitiesForPokemonIdAsync(Long id) {
        return pokemonAbilityRepository.findByPokemonIdAsync(id);
    }
    public List<PokemonAbility> getAbilitiesForPokemonId(Long id) {
        return pokemonAbilityRepository.findByPokemonId(id);
    }
}
