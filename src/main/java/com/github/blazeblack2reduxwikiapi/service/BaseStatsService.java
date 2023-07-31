package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.dto.AddBaseStatsDto;
import com.github.blazeblack2reduxwikiapi.model.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import com.github.blazeblack2reduxwikiapi.repository.BaseStatsRepository;
import com.github.blazeblack2reduxwikiapi.repository.PokemonRepository;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void addBaseStats(AddBaseStatsDto addBaseStatsDto) throws Exception {
        if (baseStatsRepository.findByPokemonName(addBaseStatsDto.getPokemonName()) == null) {
            BaseStats baseStats = new BaseStats();
            Pokemon pokemon = pokemonRepository.findByFormName(addBaseStatsDto.getPokemonName());
            if (pokemon == null) {
                throw new Exception("Could not find pokemon:\t" + addBaseStatsDto.getPokemonName());
            }
            baseStats.setPokemon(pokemon);

            // non-zero base stat => updated stat from input sheet
            if (addBaseStatsDto.getHp() != 0) {
                baseStats.setHp(addBaseStatsDto.getHp());
                baseStats.setAttack(addBaseStatsDto.getAttack());
                baseStats.setDefense(addBaseStatsDto.getDefense());
                baseStats.setSpecialAttack(addBaseStatsDto.getSpecialAttack());
                baseStats.setSpecialDefense(addBaseStatsDto.getSpecialDefense());
                baseStats.setSpeed(addBaseStatsDto.getSpeed());
                baseStats.setBst(addBaseStatsDto.getBst());
            } else {
                // zero base stat => set base stats from API
                try {
                    // set stat for each pokemon form
                    com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon = Client.getPokemonByName(addBaseStatsDto.getPokemonName());
                    baseStats.setHp(apiPokemon.getStats().get(0).getBaseStat());
                    baseStats.setAttack(apiPokemon.getStats().get(1).getBaseStat());
                    baseStats.setDefense(apiPokemon.getStats().get(2).getBaseStat());
                    baseStats.setSpecialAttack(apiPokemon.getStats().get(3).getBaseStat());
                    baseStats.setSpecialDefense(apiPokemon.getStats().get(4).getBaseStat());
                    baseStats.setSpeed(apiPokemon.getStats().get(5).getBaseStat());
                    baseStats.setBst();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(baseStats);
            baseStatsRepository.save(baseStats);
        }

    }
    public Optional<BaseStats> getBaseStatsById(Long id) {
        return baseStatsRepository.findById(id);
    }

    public long getRepositoryCount() {
        return baseStatsRepository.count();
    }
}
