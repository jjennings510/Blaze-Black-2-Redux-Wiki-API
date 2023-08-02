package com.github.blazeblack2reduxwikiapi.model.encounters;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "encounter_conditions")
public class EncounterCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @OneToMany(mappedBy = "condition")
    @JsonBackReference
    List<EncounterConditionValue> values;

    @Override
    public String toString() {
        return "EncounterCondition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
