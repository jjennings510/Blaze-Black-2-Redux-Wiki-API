package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.dto.AddMoveDto;
import com.github.blazeblack2reduxwikiapi.model.Move;
import com.github.blazeblack2reduxwikiapi.model.Type;
import com.github.blazeblack2reduxwikiapi.repository.MoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.TypeRepository;
import com.github.blazeblack2reduxwikiapi.utils.GameChanges;
import com.github.oscar0812.pokeapi.models.moves.MoveFlavorText;
import com.github.oscar0812.pokeapi.models.utility.VerboseEffect;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MoveService {
    private final TypeRepository typeRepository;
    private final MoveRepository moveRepository;

    @Autowired
    public MoveService(TypeRepository typeRepository, MoveRepository moveRepository) {
        this.typeRepository = typeRepository;
        this.moveRepository = moveRepository;
    }

    public void addMove(AddMoveDto addMoveDto) throws Exception {
        Move move = new Move();
        if (moveRepository.findByName(addMoveDto.getName()) == null) {
            // Get move from poke api
            com.github.oscar0812.pokeapi.models.moves.Move apiMove = Client.getMoveByName(addMoveDto.getName()
                    .replace(" ", "-").toLowerCase());
            if (apiMove == null) {
                throw new Exception("Could not find move:\t" + addMoveDto.getName());
            }

            move.setName(addMoveDto.getName());
            move.setIdentifier(addMoveDto.getIdentifier());
            move.setMachine(addMoveDto.getMachine());
            move.setPower(addMoveDto.getPower());
            move.setAccuracy(addMoveDto.getAccuracy());
            move.setPriority(addMoveDto.getPriority());
            move.setPp(addMoveDto.getPp());
            move.setDamageClass(addMoveDto.getDamageClass());
            move.setShortEffect(addMoveDto.getEffect());

            for (VerboseEffect effect : apiMove.getEffectEntries()) {
                if (effect.getLanguage().getName().equals("en")) {
                    move.setEffect(effect.getEffect());
                }
            }

            for (MoveFlavorText mft : apiMove.getFlavorTextEntries()) {
                // Get black 2 flavor text
                if (mft.getLanguage().getName().equals("en") &&
                        mft.getVersionGroup().getName().equals("black-2-white-2")) {
                    move.setFlavorText(mft.getFlavorText().replace("\n", " "));
                    break;
                } else if (mft.getLanguage().getName().equals("en") &&
                        mft.getVersionGroup().getName().equals("sword-shield")) {
                    // Get sword-shield flavor text if black 2 does not exist
                    move.setFlavorText(mft.getFlavorText().replace("\n", " "));
                    break;
                }
            }

            GameChanges.updateHisuiMovesDescriptions(move);

            // get type from db
            Optional<Type> type = typeRepository.findByName(addMoveDto.getTypeName());
            type.ifPresent(move::setType);

            if (move.getType() == null) {
                throw new Exception("Could not find type:\t" + addMoveDto.getTypeName());
            }
            System.out.println(move);
            moveRepository.save(move);
        }
    }
    public void updateMove(Move move) throws Exception {
        Optional<Move> newMove = moveRepository.findById(move.getId());
        if (newMove.isPresent()) {
            newMove.get().setIdentifier(move.getIdentifier());
            newMove.get().setPower(move.getPower());
            newMove.get().setAccuracy(move.getAccuracy());
            newMove.get().setPriority(move.getPriority());
            newMove.get().setPp(move.getPp());
            newMove.get().setGenerationAdded(move.getGenerationAdded());
            newMove.get().setEffectChance(move.getEffectChance());
            newMove.get().setDamageClass(move.getDamageClass());
            newMove.get().setEffect(move.getEffect());
            newMove.get().setShortEffect(move.getShortEffect());
            newMove.get().setFlavorText(move.getFlavorText());
            newMove.get().setMachine(move.getMachine());
            newMove.get().setType(move.getType());
            newMove.get().setPokemonSet(move.getPokemonSet());
            System.out.println("Updated Move:\t" + newMove);
            moveRepository.save(newMove.get());
        }

    }
    public Page<Move> getMoves(String query, String type, Pageable pageable) {
        return moveRepository.findMoves(query, type, pageable);
    }
    public long getRepositoryCount() {
        return moveRepository.count();
    }
    public Optional<Move> getMoveById(Long id) {
        return moveRepository.findById(id);
    }

    public List<Move> getMoves() {
        return moveRepository.findAll();
    }
    public Move getMoveByName(String name) {
        Move move = moveRepository.findByIdentifier(name);
        if (move == null) {
            move = moveRepository.findByName(name);
        }
        return move;
    }
    public List<Move> getAllTMs() {
        return moveRepository.findAllTMs();
    }
    public List<Move> getAllHMs() {
        return moveRepository.findAllHMs();
    }
}
