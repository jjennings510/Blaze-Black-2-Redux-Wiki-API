package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.dto.GlobalSearchDto;
import com.github.blazeblack2reduxwikiapi.model.abilities.Ability;
import com.github.blazeblack2reduxwikiapi.model.moves.Move;
import com.github.blazeblack2reduxwikiapi.model.pokemon.PokemonSpecies;
import com.github.blazeblack2reduxwikiapi.service.abilities.AbilityService;
import com.github.blazeblack2reduxwikiapi.service.moves.MoveService;
import com.github.blazeblack2reduxwikiapi.service.pokemon.PokemonSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/search")
@Controller
@CrossOrigin("http://localhost:3000")
public class SearchController {
    @Autowired
    private PokemonSpeciesService pokemonSpeciesService;
    @Autowired
    private MoveService moveService;
    @Autowired
    private AbilityService abilityService;

    @GetMapping("/get")
    public ResponseEntity<List<GlobalSearchDto>> getSuggestions(@RequestParam(name = "query") String query) {
        List<PokemonSpecies> pokemon = pokemonSpeciesService.getFirst10ByNameContaining(query);
        List<Move> moves = moveService.getFirst10ByNameContaining(query);
        List<Ability> abilities = abilityService.getFirst10ByNameContaining(query);
        List<GlobalSearchDto> dtos = new ArrayList<>();

        for (PokemonSpecies species : pokemon) {
            GlobalSearchDto dto = new GlobalSearchDto();
            dto.setEntityId(species.getId());
            dto.setName(species.getName());
            dto.setDescriptor("Pokemon");
            dtos.add(dto);
        }
        for (Move move : moves) {
            GlobalSearchDto dto = new GlobalSearchDto();
            dto.setEntityId(move.getId());
            dto.setName(move.getName());
            dto.setDescriptor("Move");
            dtos.add(dto);
        }
        for (Ability ability : abilities) {
            GlobalSearchDto dto = new GlobalSearchDto();
            dto.setEntityId(ability.getId());
            dto.setName(ability.getName());
            dto.setDescriptor("Ability");
            dtos.add(dto);
        }
        return ResponseEntity.ok()
                .body(dtos);

    }
}
