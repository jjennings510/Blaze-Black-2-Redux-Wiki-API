package com.github.blazeblack2reduxwikiapi.repository.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterConditionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface EncounterConditionValueRepository extends JpaRepository<EncounterConditionValue, Long> {
    Optional<EncounterConditionValue> findByValue(@RequestParam("value") String value);
}
