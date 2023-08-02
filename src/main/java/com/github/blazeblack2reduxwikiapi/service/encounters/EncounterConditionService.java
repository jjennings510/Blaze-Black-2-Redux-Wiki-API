package com.github.blazeblack2reduxwikiapi.service.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterCondition;
import com.github.blazeblack2reduxwikiapi.repository.encounters.EncounterConditionRepository;
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
public class EncounterConditionService {
    @Autowired
    private EncounterConditionRepository encounterConditionRepository;

    public void addEncounterCondition(EncounterCondition encounterCondition) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        EncounterCondition probe = new EncounterCondition();
        probe.setName(encounterCondition.getName());
        Example<EncounterCondition> example = Example.of(probe, modelMatcher);
        if (!encounterConditionRepository.exists(example)) {
            encounterConditionRepository.save(encounterCondition);
            System.out.println(encounterCondition);
        } else {
            System.out.println("Found matching example:\t" + encounterCondition);
        }
    }

    public List<EncounterCondition> getEncounterConditions() {
        return encounterConditionRepository.findAll();
    }

    public Optional<EncounterCondition> getEncounterConditionByName(String name) {
        return encounterConditionRepository.findByName(name);
    }
    public Optional<EncounterCondition> getEncounterConditionById(Long id) {
        return encounterConditionRepository.findById(id);
    }
}
