package com.github.blazeblack2reduxwikiapi.repository;

import com.github.blazeblack2reduxwikiapi.model.PokemonSpecies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface PokemonSpeciesRepository extends JpaRepository<PokemonSpecies, Long> {
    Optional<PokemonSpecies> findByName(@RequestParam("name") String name);
    List<PokemonSpecies> findFirst3ByIdGreaterThan(@RequestParam("id") Long id);
    Page<PokemonSpecies> findByNameContaining(@RequestParam("name") String name, Pageable pageable);
    @Query(value = "select distinct pokemon_species.* from pokemon_species " +
            "join pokemon on pokemon_species.id=pokemon.species_id " +
            "join pokemon_abilities on pokemon.id=pokemon_abilities.pokemon_id " +
            "join abilities on  pokemon_abilities.ability_id=abilities.id " +
            "join pokemon_types on  pokemon.id=pokemon_types.pokemon_id " +
            "join types on pokemon_types.type_id=types.id " +
            "where pokemon_species.name like CONCAT('%',:query,'%') or abilities.name like CONCAT('%',:query,'%') or types.name like CONCAT('%',:query,'%') ",
            countQuery = "select  count(distinct pokemon_species.id) from pokemon_species " +
                    "join pokemon on pokemon_species.id=pokemon.species_id " +
                    "join pokemon_abilities on pokemon.id=pokemon_abilities.pokemon_id " +
                    "join abilities on  pokemon_abilities.ability_id=abilities.id " +
                    "join pokemon_types on  pokemon.id=pokemon_types.pokemon_id " +
                    "join types on pokemon_types.type_id=types.id " +
                    "where pokemon_species.name like CONCAT('%',:query,'%') or abilities.name like CONCAT('%',:query,'%') or types.name like CONCAT('%',:query,'%')",
            nativeQuery = true)
    Page<PokemonSpecies> findDistinctBySearchContaining(@Param("query") String query, Pageable pageable);

    List<PokemonSpecies> findByNumberBetween(@RequestParam("start") int start, @RequestParam("end") int end);
    @Query("select p from PokemonSpecies p")
    Page<PokemonSpecies> findPage(Pageable pageable);

    @Async
    @Query("Select p from PokemonSpecies p")
    Future<List<PokemonSpecies>> findAllAsync();
}
