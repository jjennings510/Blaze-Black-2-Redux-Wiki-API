package com.github.blazeblack2reduxwikiapi.service.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterConditionValue;
import com.github.blazeblack2reduxwikiapi.repository.encounters.EncounterConditionValueRepository;
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
public class EncounterConditionValueService {
    @Autowired
    private EncounterConditionValueRepository encounterConditionValueRepository;

    public void addEncounterConditionValue(EncounterConditionValue encounterConditionValue) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("value", ignoreCase());
        EncounterConditionValue probe = new EncounterConditionValue();
        probe.setValue(encounterConditionValue.getValue());
        Example<EncounterConditionValue> example = Example.of(probe, modelMatcher);
        if (!encounterConditionValueRepository.exists(example)) {
            encounterConditionValueRepository.save(encounterConditionValue);
            System.out.println(encounterConditionValue);
        } else {
            System.out.println("Found matching example:\t" + encounterConditionValue);
        }
    }

    public List<EncounterConditionValue> getEncounterConditionValues() {
        return encounterConditionValueRepository.findAll();
    }

    public Optional<EncounterConditionValue> getEncounterConditionValueByName(String name) {
        return encounterConditionValueRepository.findByValue(name);
    }
    public Optional<EncounterConditionValue> getEncounterConditionValueById(Long id) {
        return encounterConditionValueRepository.findById(id);
    }
}
