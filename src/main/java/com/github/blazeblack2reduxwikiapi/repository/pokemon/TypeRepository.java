package com.github.blazeblack2reduxwikiapi.repository.pokemon;

import com.github.blazeblack2reduxwikiapi.model.pokemon.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(@RequestParam("name") String name);
}
