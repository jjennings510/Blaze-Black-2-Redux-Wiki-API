package com.github.blazeblack2reduxwikiapi.repository;

import com.github.blazeblack2reduxwikiapi.model.Sprite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SpriteRepository extends JpaRepository<Sprite, Long> {

    List<Sprite> findByPokemonId(@RequestParam("pokemon_id") Long pokemonId);

    Optional<Sprite> findBySpeciesIdAndSpriteType(@RequestParam("speciesId") Long speciesId,
                                                          @RequestParam("spriteType") String spriteType);
    Optional<Sprite> findFirstByPokemonIdAndSpriteType(@RequestParam("pokemonId") Long pokemonId,
                                                       @RequestParam("spriteType") String spriteType);
    @Async
    @Query("select s from Sprite s where s.species.id=:speciesId and s.spriteType=:spriteType")
    CompletableFuture<Sprite> findBySpeciesIdAndSpriteTypeAsync(@Param("speciesId") Long speciesId,
                                                                          @Param("spriteType") String spriteType);
    List<Sprite> findBySpeciesId(@RequestParam("species_id") Long speciesId);
    @Query("select s from Sprite s where s.pokemon.id=:pokemon_id and s.spriteType='artwork'")
    Optional<Sprite> findArtworkByPokemonId(@Param("pokemon_id") Long pokemonId);
    @Query("select s from Sprite s where s.spriteType=:spriteType and s.species.id in :speciesId " +
            "order by s.species.id")
    List<Sprite> findBySpriteTypeOrderByPokemonId(@Param("spriteType") String spriteType, @Param("speciesId") List<Long> speciesId);
}
