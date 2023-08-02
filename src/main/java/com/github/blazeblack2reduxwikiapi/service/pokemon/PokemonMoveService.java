package com.github.blazeblack2reduxwikiapi.service.pokemon;

import com.github.blazeblack2reduxwikiapi.model.moves.PokemonMove;
import com.github.blazeblack2reduxwikiapi.repository.moves.MoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.moves.PokemonMoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.pokemon.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

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

    public void addPokemonMove(PokemonMove pokemonMove) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("pokemon", ignoreCase())
                .withMatcher("move", ignoreCase())
                .withMatcher("method", ignoreCase());
        PokemonMove probe = new PokemonMove();
        probe.setPokemon(pokemonMove.getPokemon());
        probe.setMove(pokemonMove.getMove());
        probe.setMethod(pokemonMove.getMethod());
        Example<PokemonMove> example = Example.of(probe, modelMatcher);
        if (!pokemonMoveRepository.exists(example)) {
            pokemonMoveRepository.save(pokemonMove);
        } else {
            System.out.println("Found matching example:\t" + pokemonMove);
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
