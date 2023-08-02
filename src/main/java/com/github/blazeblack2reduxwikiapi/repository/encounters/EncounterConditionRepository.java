package com.github.blazeblack2reduxwikiapi.repository.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface EncounterConditionRepository extends JpaRepository<EncounterCondition, Long> {
    Optional<EncounterCondition> findByName(@RequestParam("name") String name);
}
