package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.model.Sprite;
import com.github.blazeblack2reduxwikiapi.service.pokemon.PokemonService;
import com.github.blazeblack2reduxwikiapi.service.SpriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/sprites")
@Controller
@CrossOrigin("http://localhost:3000")
public class SpriteController {
    private final SpriteService spriteService;
    private final PokemonService pokemonService;
    @Autowired
    public SpriteController(SpriteService spriteService, PokemonService pokemonService) {
        this.spriteService = spriteService;
        this.pokemonService = pokemonService;
    }

    @GetMapping("/all")
    public List<Sprite> getAllSpritesBySpeciesID(@RequestParam("speciesId") Long speciesId) {
        return spriteService.getAllSpritesBySpeciesId(speciesId);
    }

    @GetMapping("/get/in/speciesId")
    public ResponseEntity<List<Sprite>> getSpriteTypeInSpeciesIds(@RequestParam("spriteType") String spriteType,
                                                                   @RequestParam("id") List<Long> id) {

        List<Sprite> sprites = spriteService.GetSpriteTypeInPokemonIds(spriteType, id);
        return ResponseEntity.ok()
                .body(sprites);
    }

}
