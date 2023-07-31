package com.github.blazeblack2reduxwikiapi.repository;

import com.github.blazeblack2reduxwikiapi.model.Ability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Long> {
    Optional<Ability> findByName(@RequestParam("name") String name);
    Optional<Ability> findByIdentifier(@RequestParam("identifier") String identifier);
    Page<Ability> findAllByOrderByName(Pageable pageable);
    Page<Ability> findByNameContainingOrderByName(@RequestParam("name") String name, Pageable pageable);
}
