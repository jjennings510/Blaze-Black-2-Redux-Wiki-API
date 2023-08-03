package com.github.blazeblack2reduxwikiapi.repository.pokemon;

import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {
    @Query("Select pt from PokemonType pt join pt.pokemon p join pt.type t where p.id=:pokemonId order by pt.slot")
    List<PokemonType> findByPokemonId(@Param("pokemonId")Long pokemonId);
}
