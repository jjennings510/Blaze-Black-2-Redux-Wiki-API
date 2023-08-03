package com.github.blazeblack2reduxwikiapi.service.pokemon;

import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonType;
import com.github.blazeblack2reduxwikiapi.repository.pokemon.PokemonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PokemonTypeService {
    @Autowired
    PokemonTypeRepository pokemonTypeRepository;

    public void addPokemonType(PokemonType pokemonType) {
        List<PokemonType> types = pokemonTypeRepository.findByPokemonId(pokemonType.getPokemon().getId());
        if (!types.contains(pokemonType)) {
            System.out.println("Saving Pokemon Type:\t\t\t\t" + pokemonType);
            pokemonTypeRepository.save(pokemonType);
        }
    }

    public List<PokemonType> getPokemonTypesForPokemonId(Long id) {
        return pokemonTypeRepository.findByPokemonId(id);
    }
}
