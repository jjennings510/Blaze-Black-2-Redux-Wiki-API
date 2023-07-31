package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.dto.AddPokemonMoveDto;
import com.github.blazeblack2reduxwikiapi.model.Move;
import com.github.blazeblack2reduxwikiapi.model.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.PokemonMove;
import com.github.blazeblack2reduxwikiapi.repository.MoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.PokemonMoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PokemonMoveService {
    private final PokemonMoveRepository pokemonMoveRepository;
    private final PokemonRepository pokemonRepository;
    private final MoveRepository moveRepository;

    @Autowired
    public PokemonMoveService(PokemonMoveRepository pokemonMoveRepository, PokemonRepository pokemonRepository,
                              MoveRepository moveRepository) {
        this.pokemonMoveRepository = pokemonMoveRepository;
        this.pokemonRepository = pokemonRepository;
        this.moveRepository = moveRepository;
    }

    public void addPokemonMove(AddPokemonMoveDto addPokemonMoveDto) throws Exception {
        PokemonMove pokemonMove = new PokemonMove();
        // find pokemon in db
        Pokemon pokemon = pokemonRepository.findByFormName(addPokemonMoveDto.getPokemonName());
        if (pokemon == null) {
            throw new Exception("Could not find pokemon:\t" + addPokemonMoveDto.getPokemonName());
        }
        // find move in db
        Move move = moveRepository.findByName(addPokemonMoveDto.getMoveName());
        if (move == null) {
            move = moveRepository.findByIdentifier(addPokemonMoveDto.getMoveName());
            if (move == null) {
                throw new Exception("Could not find move:\t[" + addPokemonMoveDto.getMoveName() + "]");
            }
        }
        pokemonMove.setPokemon(pokemon);
        pokemonMove.setMove(move);

        if (pokemonMoveRepository.findByPokemonIdMoveIdForMethod(pokemon.getId(), move.getId(),
                addPokemonMoveDto.getMethod()) == null) {
            pokemonMove.setLevel(addPokemonMoveDto.getLevel());
            pokemonMove.setMethod(addPokemonMoveDto.getMethod());
            System.out.println(pokemonMove);
            pokemonMoveRepository.save(pokemonMove);
        }
    }

    public List<PokemonMove> getLevelUpMovesForPokemon(Long id) {
        return pokemonMoveRepository.findLevelUpMovesByPokemonId(id);
    }

    public List<PokemonMove> getMachinesForPokemon(Long id, String machine) {
        return pokemonMoveRepository.findMachinesByPokemonId(id, machine);
    }
    public List<PokemonMove> getTutorMovesForPokemon(Long id) {
        return pokemonMoveRepository.findMoveTutorMovesByPokemonId(id);
    }
    public List<PokemonMove> getMoveByPokemonIdMoveId(Long pokemonId, Long moveId) {
        return pokemonMoveRepository.findByPokemonIdAndMoveId(pokemonId, moveId);
    }
    public Optional<PokemonMove> getMoveByPokemonIdMoveIdForMethod(Long pokemonId, Long moveId, String method) {
        return pokemonMoveRepository.findByPokemonIdMoveIdForMethod(pokemonId, moveId, method);
    }




    public long getRepositoryCount() {
        return pokemonMoveRepository.count();
    }
}
