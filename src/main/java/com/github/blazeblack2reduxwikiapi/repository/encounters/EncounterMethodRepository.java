package com.github.blazeblack2reduxwikiapi.repository.encounters;

import com.github.blazeblack2reduxwikiapi.model.encounters.EncounterMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface EncounterMethodRepository extends JpaRepository<EncounterMethod, Long> {
    Optional<EncounterMethod> findByName(@RequestParam("name") String name);
}
