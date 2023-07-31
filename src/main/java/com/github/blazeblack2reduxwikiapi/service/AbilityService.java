package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.dto.AddAbilityDto;
import com.github.blazeblack2reduxwikiapi.model.Ability;
import com.github.blazeblack2reduxwikiapi.repository.AbilityRepository;
import com.github.oscar0812.pokeapi.models.pokemon.AbilityFlavorText;
import com.github.oscar0812.pokeapi.models.utility.Name;
import com.github.oscar0812.pokeapi.models.utility.VerboseEffect;
import com.github.oscar0812.pokeapi.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@Transactional
public class AbilityService {
    private final AbilityRepository abilityRepository;

    @Autowired
    public AbilityService(AbilityRepository abilityRepository) {
        this.abilityRepository = abilityRepository;
    }

    public void addAbility(AddAbilityDto addAbilityDto) throws Exception {
        // Check to see if ability exists
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("identifier", ignoreCase());
        Ability probe = new Ability();
        probe.setIdentifier(addAbilityDto.getIdentifier());
        Example<Ability> example = Example.of(probe, modelMatcher);
        if (!abilityRepository.exists(example)) {
            Ability ability = new Ability();
            // Get ability from api
            com.github.oscar0812.pokeapi.models.pokemon.Ability apiAbility =
                    Client.getAbilityByName(addAbilityDto.getIdentifier());
            if (apiAbility == null) {
                throw new Exception("Could not find ability:\t" + addAbilityDto.getIdentifier());
            }
            // set ability props
            ability.setIdentifier(addAbilityDto.getIdentifier());
            ability.setGenerationAdded(addAbilityDto.getGeneration());

            // grab english effect entry
            for (VerboseEffect effect : apiAbility.getEffectEntries()) {
                if (effect.getLanguage().getName().equals("en")) {
                    ability.setEffect(effect.getEffect());
                    ability.setShortEffect(effect.getShortEffect());
                    break;
                }
            }

            // grab name
            for (Name name : apiAbility.getNames()) {
                if (name.getLanguage().getName().equals("en")) {
                    ability.setName(name.getName());
                    break;
                }
            }
            // grab flavor text
            for (AbilityFlavorText flavorText : apiAbility.getFlavorTextEntries()) {
                if (flavorText.getLanguage().getName().equals("en") &&
                        flavorText.getVersionGroup().getName().equals("black-2-white-2")) {
                    ability.setFlavorText(flavorText.getFlavorText().replace("\n"," "));
                    break;
                }
            }
            abilityRepository.save(ability);
            System.out.println(ability);
        }

    }

    public Optional<Ability> getAbilityById(Long id) {
        return abilityRepository.findById(id);
    }
    public Optional<Ability> getAbilityByName(String name) {
        return abilityRepository.findByName(name);
    }
    public Optional<Ability> getAbilityByIdentifier(String identifier) {
        return abilityRepository.findByIdentifier(identifier);
    }

    public long getRepositoryCount() {
        return abilityRepository.count();
    }
}
