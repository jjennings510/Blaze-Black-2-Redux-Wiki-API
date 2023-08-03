package com.github.blazeblack2reduxwikiapi.repository.abilities;

import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PokemonAbilityRepository extends JpaRepository<PokemonAbility, Long> {
    @Query("select pa from PokemonAbility pa join pa.ability where pa.pokemon.id in :pokemonId")
    List<PokemonAbility> findByPokemonIds(@Param("pokemonId") Long[] pokemonId);
    @Async
    @Query("select pa from PokemonAbility pa join pa.ability where pa.pokemon.id = :pokemonId")
    CompletableFuture<List<PokemonAbility>> findByPokemonIdAsync(@Param("pokemonId") Long pokemonId);
    @Query("select pa from PokemonAbility pa join pa.ability where pa.pokemon.id = :pokemonId")
    List<PokemonAbility> findByPokemonId(@Param("pokemonId") Long pokemonId);
}
