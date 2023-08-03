package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.dto.abilities.AbilityDto;
import com.github.blazeblack2reduxwikiapi.dto.pokemon.MoveDetailPokemonDto;
import com.github.blazeblack2reduxwikiapi.dto.pokemon.PokemonDetailDto;
import com.github.blazeblack2reduxwikiapi.dto.pokemon.PokemonRowDto;
import com.github.blazeblack2reduxwikiapi.model.Sprite;
import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import com.github.blazeblack2reduxwikiapi.model.moves.PokemonMove;
import com.github.blazeblack2reduxwikiapi.model.pokemon.BaseStats;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonSpecies;
import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonType;
import com.github.blazeblack2reduxwikiapi.service.SpriteService;
import com.github.blazeblack2reduxwikiapi.service.abilities.PokemonAbilityService;
import com.github.blazeblack2reduxwikiapi.service.moves.PokemonMoveService;
import com.github.blazeblack2reduxwikiapi.service.pokemon.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/pokemon")
@Controller
@CrossOrigin("http://localhost:3000")
public class PokemonController {
    private final PokemonService pokemonService;
    private final PokemonSpeciesService pokemonSpeciesService;
    private final BaseStatsService baseStatsService;
    private final SpriteService spriteService;
    private final PokemonAbilityService pokemonAbilityService;
    private final PokemonMoveService pokemonMoveService;
    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonController(PokemonService pokemonService, BaseStatsService baseStatsService,
                             SpriteService spriteService, PokemonSpeciesService pokemonSpeciesService,
                             PokemonAbilityService pokemonAbilityService, PokemonMoveService pokemonMoveService,
                             PokemonTypeService pokemonTypeService) {
        this.pokemonService = pokemonService;
        this.pokemonSpeciesService = pokemonSpeciesService;
        this.baseStatsService = baseStatsService;
        this.spriteService = spriteService;
        this.pokemonAbilityService = pokemonAbilityService;
        this.pokemonMoveService = pokemonMoveService;
        this.pokemonTypeService = pokemonTypeService;
    }

    @GetMapping("get/details")
    public ResponseEntity<PokemonDetailDto> getPokemonDetails(@RequestParam("pokemonId") Long id) {
        PokemonDetailDto dto = new PokemonDetailDto();
        Optional<Pokemon> pokemon = pokemonService.getPokemonById(id);

        if (pokemon.isPresent()) {
            Long speciesId = pokemon.get().getSpecies().getId();
            List<PokemonSpecies> species = pokemonSpeciesService.getFirst3ByIdGreaterThan(speciesId - 2);

            if (speciesId == 1) {
                dto.setNextPokemonName(species.get(1).getName());
            } else if (speciesId == 649) {
                dto.setPreviousPokemonName(species.get(0).getName());
            } else {
                dto.setPreviousPokemonName(species.get(0).getName());
                dto.setNextPokemonName(species.get(2).getName());
            }
            dto.setId(pokemon.get().getId());
            dto.setName(pokemon.get().getName());
            dto.setFormName(pokemon.get().getFormName());
            dto.setNumber(pokemon.get().getNumber());

            List<PokemonType> types = pokemonTypeService.getPokemonTypesForPokemonId(pokemon.get().getId());
            List<String> typeNames = new ArrayList<>();
            for (PokemonType type : types) {
                typeNames.add(type.getType().getName());
            }
            dto.setTypes(typeNames);

            return new ResponseEntity<>(dto, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/for/ability")
    public ResponseEntity<List<PokemonRowDto>> getPokemonForAbility(@RequestParam("abilityId") Long id) {
        List<PokemonRowDto> dtos = new ArrayList<>();
        List<Pokemon> pokemonList = pokemonService.getPokemonByAbilityId(id);
        for (Pokemon pokemon : pokemonList) {
            PokemonRowDto dto = new PokemonRowDto();
            dto.setPokemonName(pokemon.getSpeciesName());
            dto.setFormName(pokemon.getFormName());
            dto.setNumber(pokemon.getNumber());

            Optional<Sprite> sprite = spriteService.getSpriteByPokemonIdAndSpriteType(pokemon.getId(), "front-default");
            sprite.ifPresent(dto::setSprite);

            List<PokemonAbility> abilities = pokemonAbilityService.getAbilitiesForPokemonId(pokemon.getId());

            for (PokemonAbility ability : abilities) {
                if (ability.getAbility().getId().equals(id)) continue;
                AbilityDto abilityDto = new AbilityDto();
                abilityDto.setId(ability.getAbility().getId());
                abilityDto.setName(ability.getAbility().getName());
                abilityDto.setShortEffect(ability.getAbility().getShortEffect());
                abilityDto.setHiddenAbility(ability.isHiddenAbility());
                dto.getAbilities().add(abilityDto);
            }
            List<PokemonType> types = pokemonTypeService.getPokemonTypesForPokemonId(pokemon.getId());
            List<String> typeNames = new ArrayList<>();
            for (PokemonType type : types) {
                typeNames.add(type.getType().getName());
            }
            dto.setTypes(typeNames);
            dtos.add(dto);
        }


        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("get/for/move")
    public ResponseEntity<Page<MoveDetailPokemonDto>> getPokemonForMove(@RequestParam("moveId") Long moveId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "20") int size,
                                                                        @RequestParam(value = "query", defaultValue = "") String query,
                                                                        @RequestParam(value = "method", defaultValue = "level-up") String method) {
        Pageable pageable = PageRequest.of(page, size);
        List<MoveDetailPokemonDto> dtos = new ArrayList<>();
        Page<Pokemon> pokemonList = pokemonService.getPokemonByMoveId(moveId, method, query, pageable);
        for (Pokemon pokemon : pokemonList) {
            MoveDetailPokemonDto dto = new MoveDetailPokemonDto();
            dto.setName(pokemon.getSpeciesName());
            dto.setFormName(pokemon.getFormName());
            dto.setNumber(pokemon.getNumber());


            Optional<Sprite> sprite = spriteService.getSpriteByPokemonIdAndSpriteType(pokemon.getId(),
                    "front-default");
            sprite.ifPresent(dto::setSprite);

            List<PokemonType> types = pokemonTypeService.getPokemonTypesForPokemonId(pokemon.getId());
            List<String> typeNames = new ArrayList<>();
            for (PokemonType type : types) {
                typeNames.add(type.getType().getName());
            }
            dto.setTypes(typeNames);

            Optional<BaseStats> baseStats = baseStatsService.getBaseStatsById(pokemon.getId());
            baseStats.ifPresent(dto::setBaseStats);

            Optional<PokemonMove> move = pokemonMoveService.getMoveByPokemonIdMoveIdForMethod(pokemon.getId(),
                    moveId, method);


            if (move.isPresent()) {
                dto.setMethod(move.get().getMethod());
                dto.setMachine(move.get().getMove().getMachine());
                dto.setLevel(move.get().getLevel());
            }
            dtos.add(dto);
        }
        Page<MoveDetailPokemonDto> dtoPage = new PageImpl<>(dtos, pageable, pokemonList.getTotalElements());

        return ResponseEntity.ok()
                .body(dtoPage);
    }
}
