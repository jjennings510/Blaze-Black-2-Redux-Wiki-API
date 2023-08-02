package com.github.blazeblack2reduxwikiapi.service.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterMethod;
import com.github.blazeblack2reduxwikiapi.repository.encounters.EncounterMethodRepository;
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
public class EncounterMethodService {
    @Autowired
    private EncounterMethodRepository encounterMethodRepository;

    public void addEncounterMethod(EncounterMethod encounterMethod) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        EncounterMethod probe = new EncounterMethod();
        probe.setName(encounterMethod.getName());
        Example<EncounterMethod> example = Example.of(probe, modelMatcher);
        if (!encounterMethodRepository.exists(example)) {
            encounterMethodRepository.save(encounterMethod);
            System.out.println(encounterMethod);
        } else {
            System.out.println("Found matching example:\t" + encounterMethod);
        }
    }

    public List<EncounterMethod> getEncounterMethods() {
        return encounterMethodRepository.findAll();
    }

    public Optional<EncounterMethod> getEncounterMethodByName(String name) {
        return encounterMethodRepository.findByName(name);
    }
    public Optional<EncounterMethod> getEncounterMethodById(Long id) {
        return encounterMethodRepository.findById(id);
    }
}
