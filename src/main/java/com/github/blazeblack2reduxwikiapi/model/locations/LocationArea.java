package com.github.blazeblack2reduxwikiapi.model.locations;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "location_areas")
public class LocationArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String displayName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @JsonManagedReference
    Location location;
    @OneToMany(mappedBy = "locationArea", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<PokemonEncounter> encounters;

    @Override
    public String toString() {
        return "LocationArea{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
