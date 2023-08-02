package com.github.blazeblack2reduxwikiapi.service.abilities;

import com.github.blazeblack2reduxwikiapi.model.abilities.Ability;
import com.github.blazeblack2reduxwikiapi.repository.abilities.AbilityRepository;
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

    public void addAbility(Ability ability) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Ability probe = new Ability();
        probe.setName(ability.getName());
        Example<Ability> example = Example.of(probe, modelMatcher);
        if (!abilityRepository.exists(example)) {
            abilityRepository.save(ability);
        } else {
            System.out.println("Found matching example:\t" + ability);
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
