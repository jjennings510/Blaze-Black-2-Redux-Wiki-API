package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.dto.abilities.AbilityDto;
import com.github.blazeblack2reduxwikiapi.dto.abilities.PokemonAbilityDto;
import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import com.github.blazeblack2reduxwikiapi.service.abilities.AbilityService;
import com.github.blazeblack2reduxwikiapi.service.pokemon.PokemonAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/abilities")
public class AbilityController {
    private PokemonAbilityService pokemonAbilityService;
    private AbilityService abilityService;

    @Autowired
    public AbilityController(PokemonAbilityService pokemonAbilityService, AbilityService abilityService) {
        this.pokemonAbilityService = pokemonAbilityService;
        this.abilityService = abilityService;
    }

    @GetMapping("/get/in/pokemonIds")
    public ResponseEntity<List<PokemonAbilityDto>> getAbilitiesInPokemonIds(@RequestParam("ids") List<Long> ids) {
        List<PokemonAbility> pokemonAbilities = pokemonAbilityService.getAbilitiesForPokemonIds(ids.toArray(new Long[0]));
        List<PokemonAbilityDto> response = new ArrayList<>();

        for (Long id : ids) {
            List<AbilityDto> dtos = new ArrayList<>();
            PokemonAbilityDto pokemonAbilityDto = new PokemonAbilityDto();
            for (PokemonAbility pokemonAbility : pokemonAbilities) {
                if (Objects.equals(id, pokemonAbility.getPokemon().getId())) {
                    AbilityDto abilityDto = new AbilityDto();
                    abilityDto.setId(pokemonAbility.getAbility().getId());
                    abilityDto.setPokemonId(pokemonAbility.getPokemon().getId());
                    abilityDto.setName(pokemonAbility.getAbility().getName());
                    abilityDto.setShortEffect(pokemonAbility.getAbility().getShortEffect());
                    abilityDto.setHiddenAbility(pokemonAbility.isHiddenAbility());
                    dtos.add(abilityDto);
                }
            }
            pokemonAbilityDto.setAbilities(dtos);
            pokemonAbilityDto.setPokemonId(id);
            response.add(pokemonAbilityDto);
        }
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("get/for/pokemonId")
    public ResponseEntity<List<AbilityDto>> getAbilitiesForPokemonId(@RequestParam("pokemonId") Long pokemonId) {
        List<PokemonAbility> pokemonAbilities = pokemonAbilityService.getAbilitiesForPokemonId(pokemonId);
        List<AbilityDto> abilityDtos = new ArrayList<>();

        for (PokemonAbility ability : pokemonAbilities) {
            AbilityDto abilityDto = new AbilityDto();
            abilityDto.setId(ability.getAbility().getId());
            abilityDto.setName(ability.getAbility().getName());
            abilityDto.setShortEffect(ability.getAbility().getShortEffect());
            abilityDto.setHiddenAbility(ability.isHiddenAbility());
            abilityDtos.add(abilityDto);
        }
        return ResponseEntity.ok()
                .body(abilityDtos);
    }
}
