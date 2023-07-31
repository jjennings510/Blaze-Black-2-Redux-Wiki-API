package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.dto.AbilityDto;
import com.github.blazeblack2reduxwikiapi.dto.PokemonRowDto;
import com.github.blazeblack2reduxwikiapi.model.*;
import com.github.blazeblack2reduxwikiapi.service.PokemonAbilityService;
import com.github.blazeblack2reduxwikiapi.service.PokemonService;
import com.github.blazeblack2reduxwikiapi.service.PokemonSpeciesService;
import com.github.blazeblack2reduxwikiapi.service.SpriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/pokemonSpecies")
public class PokemonSpeciesController {
    PokemonSpeciesService pokemonSpeciesService;
    SpriteService spriteService;
    PokemonAbilityService pokemonAbilityService;
    PokemonService pokemonService;

    public PokemonSpeciesController(PokemonSpeciesService pokemonSpeciesService, SpriteService spriteService,
                                    PokemonAbilityService pokemonAbilityService, PokemonService pokemonService) {
        this.pokemonSpeciesService = pokemonSpeciesService;
        this.spriteService = spriteService;
        this.pokemonAbilityService = pokemonAbilityService;
        this.pokemonService = pokemonService;
    }

    @GetMapping("/get/pokedex")
    public ResponseEntity<Page<PokemonRowDto>> getPokemonRow(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "20") int size,
                                                             @RequestParam(value = "query", defaultValue = "") String query) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PokemonSpecies> list;
        if (query.equals("")) {
            list = pokemonSpeciesService.getPokemonPage(pageable);
        } else {
            list = pokemonSpeciesService.getPokemonByQuery(query, pageable);
        }

        List<PokemonRowDto> dtos = new ArrayList<>();
        for (PokemonSpecies pokemonSpecies : list) {
            PokemonRowDto dto = new PokemonRowDto();
            Optional<Sprite> sprite = spriteService.getSpriteBySpeciesIdAndSpriteType(pokemonSpecies.getId(),
                    "front-default");
            sprite.ifPresent(dto::setSprite);
            Pokemon pokemon = pokemonService.getFirstPokemonBySpeciesId(pokemonSpecies.getId());
            dto.setPokemonName(pokemonSpecies.getName());
            dto.setFormName(pokemon.getFormName());
            dto.setNumber(pokemonSpecies.getNumber());

            List<PokemonAbility> abilities = pokemonAbilityService.getAbilitiesForPokemonId(pokemon.getId());
            for (PokemonAbility ability : abilities) {
                AbilityDto abilityDto = new AbilityDto();
                abilityDto.setId(ability.getAbility().getId());
                abilityDto.setName(ability.getAbility().getName());
                abilityDto.setShortEffect(ability.getAbility().getShortEffect());
                abilityDto.setHiddenAbility(ability.isHiddenAbility());
                dto.getAbilities().add(abilityDto);
            }

            for (Type type : pokemon.getTypes()) {
                dto.getTypes().add(type.getName());
            }
            dto.setBaseStats(pokemon.getBaseStats());
            dtos.add(dto);
        }

        Page<PokemonRowDto> dtoPage = new PageImpl<>(dtos, pageable, list.getTotalElements());

        return ResponseEntity.ok()
                .body(dtoPage);
    }
}
