package com.github.blazeblack2reduxwikiapi.service.pokemon;

import com.github.blazeblack2reduxwikiapi.model.pokemon.BaseStats;
import com.github.blazeblack2reduxwikiapi.repository.pokemon.BaseStatsRepository;
import com.github.blazeblack2reduxwikiapi.repository.pokemon.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@Transactional
public class BaseStatsService {
    private final BaseStatsRepository baseStatsRepository;
    private final PokemonRepository pokemonRepository;

    @Autowired
    public BaseStatsService(BaseStatsRepository baseStatsRepository, PokemonRepository pokemonRepository) {
        this.baseStatsRepository = baseStatsRepository;
        this.pokemonRepository = pokemonRepository;
    }

    public void addBaseStats(BaseStats baseStats) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("pokemon", ignoreCase());
        BaseStats probe = new BaseStats();
        probe.setPokemon(baseStats.getPokemon());
        Example<BaseStats> example = Example.of(probe, modelMatcher);
        if (!baseStatsRepository.exists(example)) {
            baseStatsRepository.save(baseStats);
        } else {
            System.out.println("Found matching example:\t" + baseStats);
        }
    }

    public Optional<BaseStats> getBaseStatsById(Long id) {
        return baseStatsRepository.findById(id);
    }

    public long getRepositoryCount() {
        return baseStatsRepository.count();
    }
}
