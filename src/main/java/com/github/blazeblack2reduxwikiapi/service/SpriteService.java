package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.PokemonSpecies;
import com.github.blazeblack2reduxwikiapi.model.Sprite;
import com.github.blazeblack2reduxwikiapi.repository.PokemonSpeciesRepository;
import com.github.blazeblack2reduxwikiapi.repository.SpriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@Transactional
public class SpriteService {
    private SpriteRepository spriteRepository;
    private PokemonSpeciesRepository pokemonSpeciesRepository;

    @Autowired
    public SpriteService(SpriteRepository spriteRepository, PokemonSpeciesRepository pokemonSpeciesRepository) {
        this.spriteRepository = spriteRepository;
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
    }

    public void addSprite(Sprite sprite) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Sprite probe = new Sprite();
        probe.setName(sprite.getName());
        probe.setSpriteType(sprite.getSpriteType());
        Example<Sprite> example = Example.of(probe, modelMatcher);
        if (!spriteRepository.exists(example)) {
            spriteRepository.save(sprite);
        } else {
            System.out.println("Found matching example:\t" + sprite);
        }
    }
    public Long getSpriteRepositoryCount() {
        return  spriteRepository.count();
    }
    public List<Sprite> getSpritesByPokemonId(Long id) {
        return spriteRepository.findByPokemonId(id);
    }
    public List<Sprite> getSpritesBySpeciesId(Long id) {
        return spriteRepository.findBySpeciesId(id);
    }
    public List<Sprite> getAllSpritesBySpeciesId(Long id) {
        Optional<PokemonSpecies> species = pokemonSpeciesRepository.findById(id);
        List<Sprite> sprites = new ArrayList<>();
        if (species.isPresent()) {
            for (Pokemon pokemon : species.get().getVarieties()) {
                sprites.addAll(pokemon.getSprites());
            }
        }
        return sprites;
    }
    public List<Sprite> GetSpriteTypeInPokemonIds(String spriteType, List<Long> ids) {
        return spriteRepository.findBySpriteTypeOrderByPokemonId(spriteType, ids);
    }
    public Optional<Sprite> getSpriteBySpeciesIdAndSpriteType(Long id, String spriteType) {
        return spriteRepository.findBySpeciesIdAndSpriteType(id, spriteType);
    }
    public Optional<Sprite> getSpriteByPokemonIdAndSpriteType(Long id, String spriteType) {
        return spriteRepository.findFirstByPokemonIdAndSpriteType(id, spriteType);
    }
    public Optional<Sprite> getArtworkByPokemonId(Long id) {
        return spriteRepository.findArtworkByPokemonId(id);
    }
    public void updateSpritePokemon(Sprite sprite) {
        Optional<Sprite> update = spriteRepository.findById(sprite.getId());
        if (update.isPresent()) {
            update.get().setPokemon(sprite.getPokemon());
            spriteRepository.save(update.get());
        }
    }


}
