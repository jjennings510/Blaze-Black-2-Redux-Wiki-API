package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.model.PokemonSpecies;
import com.github.blazeblack2reduxwikiapi.repository.PokemonSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@Transactional
public class PokemonSpeciesService {
    private final PokemonSpeciesRepository pokemonSpeciesRepository;

    @Autowired
    public PokemonSpeciesService(PokemonSpeciesRepository pokemonSpeciesRepository) {
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
    }

    public void addPokemonSpecies(PokemonSpecies species) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        PokemonSpecies probe = new PokemonSpecies();
        probe.setName(species.getName());
        Example<PokemonSpecies> example = Example.of(probe, modelMatcher);
        if (!pokemonSpeciesRepository.exists(example)) {
            pokemonSpeciesRepository.save(species);
        } else {
            System.out.println("Found matching example:\t" + species);
        }
    }
    public List<PokemonSpecies> getPokemonSpecies() {
        return pokemonSpeciesRepository.findAll();
    }
    public List<PokemonSpecies> getPokemonByNumberBetween(int start, int end) {
        return pokemonSpeciesRepository.findByNumberBetween(start, end);
    }
    public Page<PokemonSpecies> getPokemonPage(Pageable pageable) {
        return pokemonSpeciesRepository.findPage(pageable);
    }
    public Page<PokemonSpecies> getPokemonByQuery(String query, Pageable pageable) {
        return pokemonSpeciesRepository.findDistinctBySearchContaining(query, pageable);
    }

    public Page<PokemonSpecies> getPokemonSpecies(int page, int size) {
        Pageable pageRequest = createPageRequestUsing(page, size);

        List<PokemonSpecies> allSpecies = pokemonSpeciesRepository.findAll();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), allSpecies.size());

        List<PokemonSpecies> pageContent = allSpecies.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, allSpecies.size());
    }
    public Optional<PokemonSpecies> getPokemonSpeciesByName(String name) {
        return pokemonSpeciesRepository.findByName(name);
    }
    public Optional<PokemonSpecies> getPokemonSpeciesById(Long id) {
        return pokemonSpeciesRepository.findById(id);
    }
    public List<PokemonSpecies> getFirst3ByIdGreaterThan(Long id) {
        return pokemonSpeciesRepository.findFirst3ByIdGreaterThan(id);
    }
    public Long getRepositoryCount() {
        return pokemonSpeciesRepository.count();
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }


}
