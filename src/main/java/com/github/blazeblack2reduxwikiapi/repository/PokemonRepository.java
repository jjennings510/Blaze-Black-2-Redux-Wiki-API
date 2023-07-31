package com.github.blazeblack2reduxwikiapi.repository;

import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RepositoryRestResource(collectionResourceRel = "pokemon", path = "pokemon")
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    Page<Pokemon> findBySpeciesNameContaining(@RequestParam("speciesName") String speciesName, Pageable pageable);
    List<Pokemon> findByName(@RequestParam("name") String name);
    Pokemon findByFormName(@RequestParam("formName") String formName);
    List<Pokemon> findByNumber(@RequestParam("number") int number);
    List<Pokemon> findBySpeciesName(@RequestParam("speciesName") String speciesName);
    @Query("select p from Pokemon p where p.species.id=:speciesId")
    List<Pokemon> findBySpeciesId(@RequestParam("speciesId") Long speciesId);
    @Query("""
            select p from Pokemon p join p.abilities pa
            join p.abilities.ability a
            where a.id=:abilityId""")
    List<Pokemon> findByAbilityId(@Param("abilityId") Long abilityId);
    @Query("""
            select p from Pokemon p join p.moves pm
            join p.moves.move m
            where m.id=:moveId and p.name like %:query%
            and pm.method=:method""")
    Page<Pokemon> findByMoveIdForMethod(@Param("moveId") Long moveId, String method, String query, Pageable pageable);
    @Async
    @Query("select p from Pokemon p where p.species.id=:speciesId order by p.id limit 1")
    CompletableFuture<Pokemon> findFirstBySpeciesIdAsync(@Param("speciesId") Long speciesId);

    @Query("select p from Pokemon p where p.species.id=:speciesId order by p.id limit 1")
    Pokemon findFirstBySpeciesId(@Param("speciesId") Long speciesId);

}
