package com.github.blazeblack2reduxwikiapi.service.locations;

import com.github.blazeblack2reduxwikiapi.model.locations.LocationArea;
import com.github.blazeblack2reduxwikiapi.repository.locations.LocationAreaRepository;
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
public class LocationAreaService {
    @Autowired
    LocationAreaRepository locationAreaRepository;

    public void addLocationArea(LocationArea locationArea) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        LocationArea probe = new LocationArea();
        probe.setName(locationArea.getName());
        Example<LocationArea> example = Example.of(probe, modelMatcher);
        if (!locationAreaRepository.exists(example)) {
            locationAreaRepository.save(locationArea);
            System.out.println(locationArea);
        } else {
            System.out.println("Found matching example:\t" + locationArea);
        }
    }

    public List<LocationArea> getLocationAreas() {
        return locationAreaRepository.findAll();
    }
    public Optional<LocationArea> getLocationAreaByName(String name) {
        return locationAreaRepository.findByName(name);
    }
}
