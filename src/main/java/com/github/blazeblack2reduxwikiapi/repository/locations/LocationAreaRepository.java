package com.github.blazeblack2reduxwikiapi.repository.locations;

import com.github.blazeblack2reduxwikiapi.model.locations.LocationArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface LocationAreaRepository extends JpaRepository<LocationArea, Long> {
    Optional<LocationArea> findByName(@RequestParam("name") String name);
}
