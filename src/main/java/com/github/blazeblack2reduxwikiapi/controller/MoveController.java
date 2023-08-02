package com.github.blazeblack2reduxwikiapi.controller;

import com.github.blazeblack2reduxwikiapi.dto.moves.LevelUpMovesDto;
import com.github.blazeblack2reduxwikiapi.dto.moves.MachineMovesDto;
import com.github.blazeblack2reduxwikiapi.dto.moves.MoveDetailDto;
import com.github.blazeblack2reduxwikiapi.dto.moves.MoveRowDto;
import com.github.blazeblack2reduxwikiapi.model.moves.Move;
import com.github.blazeblack2reduxwikiapi.model.moves.PokemonMove;
import com.github.blazeblack2reduxwikiapi.service.moves.MoveService;
import com.github.blazeblack2reduxwikiapi.service.pokemon.PokemonMoveService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/moves")
public class MoveController {
    private PokemonMoveService pokemonMoveService;
    private MoveService moveService;

    @Autowired
    public MoveController(PokemonMoveService pokemonMoveService, MoveService moveService) {
        this.pokemonMoveService = pokemonMoveService;
        this.moveService = moveService;
    }

    @GetMapping("/get/levelUp")
    public List<LevelUpMovesDto> getLevelUpMoves(@RequestParam("pokemonId") Long id) {
        List<PokemonMove> pokemonMoves = pokemonMoveService.getLevelUpMovesForPokemon(id);
        List<LevelUpMovesDto> dtos = new ArrayList<>();
        for (PokemonMove move : pokemonMoves) {
            LevelUpMovesDto dto = new LevelUpMovesDto();
            dto.setId(move.getMove().getId());
            dto.setLevel(move.getLevel());
            dto.setMove(move.getMove().getName());
            dto.setType(move.getMove().getType().getName());
            dto.setCategory(move.getMove().getDamageClass());
            dto.setPower(move.getMove().getPower());
            dto.setAccuracy(move.getMove().getAccuracy());
            dto.setDescription(move.getMove().getShortEffect());
            dtos.add(dto);
        }
        return dtos;
    }

    @GetMapping("/get/machines")
    public List<MachineMovesDto> getMachineMoves(@RequestParam("pokemonId") Long id) {
        List<PokemonMove> TMs = pokemonMoveService.getMachinesForPokemon(id, "TM");
        List<PokemonMove> HMs = pokemonMoveService.getMachinesForPokemon(id, "HM");
        List<PokemonMove> tutor = pokemonMoveService.getTutorMovesForPokemon(id);
        List<MachineMovesDto> dtos = new ArrayList<>();
        for (PokemonMove move : TMs) {
            MachineMovesDto dto = new MachineMovesDto();
            dto.setId(move.getMove().getId());
            dto.setMachine(move.getMove().getMachine());
            dto.setMove(move.getMove().getName());
            dto.setType(move.getMove().getType().getName());
            dto.setCategory(move.getMove().getDamageClass());
            dto.setPower(move.getMove().getPower());
            dto.setAccuracy(move.getMove().getAccuracy());
            dto.setDescription(move.getMove().getShortEffect());
            dtos.add(dto);
        }
        for (PokemonMove move : HMs) {
            MachineMovesDto dto = new MachineMovesDto();
            dto.setId(move.getMove().getId());
            dto.setMachine(move.getMove().getMachine());
            dto.setMove(move.getMove().getName());
            dto.setType(move.getMove().getType().getName());
            dto.setCategory(move.getMove().getDamageClass());
            dto.setPower(move.getMove().getPower());
            dto.setAccuracy(move.getMove().getAccuracy());
            dto.setDescription(move.getMove().getShortEffect());
            dtos.add(dto);
        }
        for (PokemonMove move : tutor) {
            MachineMovesDto dto = new MachineMovesDto();
            dto.setId(move.getMove().getId());
            dto.setMachine(move.getMove().getMachine());
            dto.setMove(move.getMove().getName());
            dto.setType(move.getMove().getType().getName());
            dto.setCategory(move.getMove().getDamageClass());
            dto.setPower(move.getMove().getPower());
            dto.setAccuracy(move.getMove().getAccuracy());
            dto.setDescription(move.getMove().getShortEffect());
            dtos.add(dto);
        }
        return dtos;
    }

    @GetMapping("/get/all")
    public ResponseEntity<Page<MoveRowDto>> getMoves(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                                     @RequestParam(value = "query", defaultValue = "") String query,
                                                     @RequestParam(value = "type", defaultValue = "") String type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Move> list = moveService.getMoves(query, type, pageable);
        List<MoveRowDto> dtos = new ArrayList<>();
        for (Move move : list) {
            MoveRowDto dto = new MoveRowDto();
            dto.setId(move.getId());
            dto.setName(move.getName());
            dto.setEffect(move.getShortEffect());
            dto.setType(move.getType().getName());
            dto.setCategory(move.getDamageClass());
            dto.setPower(move.getPower());
            dto.setAccuracy(move.getAccuracy());
            dtos.add(dto);
        }
        Page<MoveRowDto> dtoPage = new PageImpl<>(dtos, pageable, list.getTotalElements());

        return ResponseEntity.ok()
                .body(dtoPage);
    }

    @GetMapping("/get/details")
    public ResponseEntity<MoveDetailDto> getMoveDetails(@RequestParam("moveId") Long id) {
        Optional<Move> move = moveService.getMoveById(id);
        if (move.isPresent()) {
            MoveDetailDto dto = new MoveDetailDto();
            dto.setId(move.get().getId());
            dto.setName(move.get().getName());
            dto.setPower(move.get().getPower());
            dto.setAccuracy(move.get().getAccuracy());
            dto.setPriority(move.get().getPriority());
            dto.setPp(move.get().getPp());
            dto.setGenerationAdded(move.get().getGenerationAdded());
            dto.setEffectChance(move.get().getEffectChance());
            dto.setCategory(move.get().getDamageClass());
            dto.setEffect(move.get().getEffect());
            dto.setFlavorText(move.get().getFlavorText());
            dto.setMachine(move.get().getMachine());
            dto.setType(move.get().getType().getName());
            return ResponseEntity.ok()
                    .body(dto);
        }
        return ResponseEntity.notFound()
                .build();
    }
}
