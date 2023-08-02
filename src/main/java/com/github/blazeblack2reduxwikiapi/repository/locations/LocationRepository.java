package com.github.blazeblack2reduxwikiapi.repository.locations;

import com.github.blazeblack2reduxwikiapi.model.locations.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(@RequestParam("name") String name);
}
