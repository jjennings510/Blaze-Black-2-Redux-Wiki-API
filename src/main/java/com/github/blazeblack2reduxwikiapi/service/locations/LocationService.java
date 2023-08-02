package com.github.blazeblack2reduxwikiapi.service.locations;

import com.github.blazeblack2reduxwikiapi.model.locations.Location;
import com.github.blazeblack2reduxwikiapi.repository.locations.LocationRepository;
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
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public void addLocation(Location location) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Location probe = new Location();
        probe.setName(location.getName());
        Example<Location> example = Example.of(probe, modelMatcher);
        if (!locationRepository.exists(example)) {
            locationRepository.save(location);
            System.out.println(location);
        } else {
            System.out.println("Found matching example:\t" + location);
        }
    }

    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationByName(String name) {
        return locationRepository.findByName(name);
    }
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
}
