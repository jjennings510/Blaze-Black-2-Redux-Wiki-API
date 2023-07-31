package com.github.blazeblack2reduxwikiapi.repository;

import com.github.blazeblack2reduxwikiapi.model.PokemonMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PokemonMoveRepository extends JpaRepository<PokemonMove, Long> {
    @Query("select m from PokemonMove m where m.pokemon.id = :pokemon_id and m.move.id = :move_id")
    List<PokemonMove> findByPokemonIdAndMoveId(@Param("pokemon_id") Long pokemonId, @Param("move_id") Long moveId);

    @Query("select m from PokemonMove m where m.pokemon.id = :pokemon_id and m.move.id = :move_id and m.method = :method")
    Optional<PokemonMove> findByPokemonIdMoveIdForMethod(@Param("pokemon_id") Long pokemonId, @Param("move_id") Long moveId,
                                                         @Param("method") String method);

    @Query("""
            select p from PokemonMove p
            left join fetch p.move
            where p.pokemon.id=:pokemon_id
            and p.method='level-up'""")
    List<PokemonMove> findLevelUpMovesByPokemonId(@Param("pokemon_id") Long pokemonId);
    @Query("""
            select p from PokemonMove p
            left join fetch p.move
            where p.pokemon.id=:pokemon_id
            and p.method='machine'
            and p.move.machine like :machine%
            order by p.move.machine asc""")
    List<PokemonMove> findMachinesByPokemonId(@Param("pokemon_id") Long pokemonId, @Param("machine") String machine);
    @Query("""
            select p from PokemonMove p
            left join fetch p.move
            where p.pokemon.id=:pokemon_id
            and p.method='tutor'
            order by p.move.name asc""")
    List<PokemonMove> findMoveTutorMovesByPokemonId(@Param("pokemon_id") Long pokemonId);
}
