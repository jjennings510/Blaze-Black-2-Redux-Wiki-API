package com.github.blazeblack2reduxwikiapi.service.pokemon;

import com.github.blazeblack2reduxwikiapi.model.abilities.Ability;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Pokemon;
import com.github.blazeblack2reduxwikiapi.model.abilities.PokemonAbility;
import com.github.blazeblack2reduxwikiapi.model.pokemon.Type;
import com.github.blazeblack2reduxwikiapi.repository.*;
import com.github.blazeblack2reduxwikiapi.repository.abilities.AbilityRepository;
import com.github.blazeblack2reduxwikiapi.repository.abilities.PokemonAbilityRepository;
import com.github.blazeblack2reduxwikiapi.repository.moves.PokemonMoveRepository;
import com.github.blazeblack2reduxwikiapi.repository.pokemon.*;
import com.github.oscar0812.pokeapi.models.pokemon.PokemonType;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final AbilityRepository abilityRepository;
    private final TypeRepository typeRepository;
    private final PokemonAbilityRepository pokemonAbilityRepository;
    private final SpriteRepository spriteRepository;
    private final PokemonMoveRepository pokemonMoveRepository;
    private final BaseStatsRepository baseStatsRepository;

    @Autowired
    public PokemonService(PokemonRepository pokemonRepository, AbilityRepository abilityRepository, TypeRepository typeRepository,
                          PokemonAbilityRepository pokemonAbilityRepository, SpriteRepository spriteRepository,
                          PokemonMoveRepository pokemonMoveRepository, BaseStatsRepository baseStatsRepository) {
        this.pokemonRepository = pokemonRepository;
        this.abilityRepository = abilityRepository;
        this.typeRepository = typeRepository;
        this.pokemonAbilityRepository = pokemonAbilityRepository;
        this.spriteRepository = spriteRepository;
        this.pokemonMoveRepository = pokemonMoveRepository;
        this.baseStatsRepository = baseStatsRepository;
    }

//    public void addPokemon(AddPokemonDto addPokemonDto) throws Exception {
//        if (pokemonRepository.findByFormName(addPokemonDto.getFormName()) == null) {
//            Pokemon pokemon = new Pokemon();
//            com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon =
//                    Client.getPokemonByName(addPokemonDto.getFormName());
//
//            if (apiPokemon == null) {
//                throw new Exception("Could not find pokemon:\t" + addPokemonDto.getFormName());
//            }
//            // extract the english name and set to Pokémon
//            for (Name name : apiPokemon.getSpecies().getNames()) {
//                if (name.getLanguage().getName().equals("en")) {
//                    pokemon.setSpeciesName(name.getName());
//                    break;
//                }
//            }
//            pokemon.setNumber(addPokemonDto.getNumber());
//            pokemon.setFormName(addPokemonDto.getFormName());
//            setAbilities(addPokemonDto.getAbilityNames(), pokemon, apiPokemon);
//            setTypes(addPokemonDto.getTypeNames(), pokemon, apiPokemon);
//            System.out.println(pokemon);
//            pokemonRepository.save(pokemon);
//        }
//    }

    public List<Pokemon> getPokemon() {
        return pokemonRepository.findAll();
    }

    public List<Pokemon> getPokemonByNumber(int number) {
        return pokemonRepository.findByNumber(number);
    }

    public Optional<Pokemon> getPokemonByFormName(String formName) {
        return pokemonRepository.findByFormName(formName);
    }

    public List<Pokemon> getPokemonByName(String name) {
        return pokemonRepository.findByName(name);
    }

    public List<Pokemon> getPokemonBySpeciesName(String speciesName) {
        return pokemonRepository.findBySpeciesName(speciesName);
    }

    public List<Pokemon> getPokemonByAbilityId(Long id) {
        return pokemonRepository.findByAbilityId(id);
    }

    public Page<Pokemon> getPokemonByMoveId(Long id, String method, String query, Pageable pageable) {

        return pokemonRepository.findByMoveIdForMethod(id, method, query, pageable);
    }

    public Optional<Pokemon> getPokemonById(Long id) {
        return pokemonRepository.findById(id);
    }

    public Pokemon getFirstPokemonBySpeciesId(Long id) {
        return pokemonRepository.findFirstBySpeciesId(id);
    }

    public long getRepositoryCount() {
        return pokemonRepository.count();
    }

    public void deletePokemon(Long id) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(id);
        pokemon.ifPresent(value -> pokemonRepository.deleteById(value.getId()));
    }

    public void updatePokemonSpecies(Pokemon pokemon) {
        Optional<Pokemon> update = pokemonRepository.findById(pokemon.getId());
        if (update.isPresent()) {
            update.get().setSpecies(pokemon.getSpecies());
            update.get().setSpeciesName(pokemon.getSpeciesName());
            pokemonRepository.save(update.get());
        }
    }

    public void updatePokemonType(Pokemon pokemon) {
        Optional<Pokemon> update = pokemonRepository.findById(pokemon.getId());
        if (update.isPresent()) {
            update.get().setTypes(pokemon.getTypes());
            pokemonRepository.save(update.get());
        }
    }

    public void updatePokemonAbilities(Pokemon pokemon) {
        Optional<Pokemon> update = pokemonRepository.findById(pokemon.getId());
        if (update.isPresent()) {
            update.get().setAbilities(pokemon.getAbilities());
            pokemonRepository.save(update.get());
        }
    }

    private void setTypes(List<String> typeNames, Pokemon pokemon, com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon) throws Exception {
        List<Type> types = new ArrayList<>();
        if (typeNames != null) {
            for (String typeName : typeNames) {
                Optional<Type> type = typeRepository.findByName(typeName);
                if (type.isEmpty()) {
                    throw new Exception("Could not find type:\t" + typeName);
                }
                types.add(type.get());
            }
        } else {
            // fetch types from api
            for (PokemonType pokemonType : Client.getPokemonByName(pokemon.getFormName()).getTypes()) {
                Optional<Type> type = typeRepository.findByName(pokemonType.getType().getName());
                if (type.isEmpty()) {
                    throw new Exception("Could not find type:\t" + pokemonType.getType().getName());
                }
                types.add(type.get());
            }
        }
        pokemon.setTypes(types);
    }

    private void setAbilities(List<String> abilityNames, Pokemon pokemon,
                              com.github.oscar0812.pokeapi.models.pokemon.Pokemon apiPokemon) throws Exception {
        List<PokemonAbility> abilities = new ArrayList<>();
        int index = 0;
        if (abilityNames != null) {
            // updated abilities, get from array
            for (String abilityName : abilityNames) {
                if (!abilityName.equals("-")) {
                    PokemonAbility pokemonAbility = new PokemonAbility();
                    Optional<Ability> ability = abilityRepository.findByName(abilityName);
                    if (ability.isEmpty()) {
                        throw new Exception("Could not find ability:\t" + abilityName);
                    }
                    pokemonAbility.setAbility(ability.get());
                    pokemonAbility.setPokemon(pokemon);
                    pokemonAbility.setHiddenAbility(index == 2);
                    abilities.add(pokemonAbility);
                }
                index++;
            }
        } else {
            // get ability data from api
            for (com.github.oscar0812.pokeapi.models.pokemon.PokemonAbility apiPokemonAbility : apiPokemon.getAbilities()) {
                PokemonAbility ability = new PokemonAbility();
                Optional<Ability> ab = abilityRepository.findByName(apiPokemonAbility.getAbility().getName().replace("-", " "));

                if (ab.isEmpty()) {
                    throw new Exception("Could not find ability:\t" + apiPokemonAbility.getAbility().getName());
                }
                ability.setAbility(ab.get());
                ability.setPokemon(pokemon);
                ability.setHiddenAbility(index == 2);
                abilities.add(ability);
                index++;
            }
        }
        pokemon.setAbilities(abilities);
    }


}
