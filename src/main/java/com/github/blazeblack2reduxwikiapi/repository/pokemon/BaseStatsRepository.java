package com.github.blazeblack2reduxwikiapi.repository.pokemon;

import com.github.blazeblack2reduxwikiapi.model.pokemon.BaseStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "baseStats", path = "baseStats")
public interface BaseStatsRepository extends JpaRepository<BaseStats, Long> {
    @Query("select b from BaseStats b inner join Pokemon p on b.id = p.id where p.formName = :pokemon_name")
    BaseStats findByPokemonName(@Param("pokemon_name") String pokemonName);
}
