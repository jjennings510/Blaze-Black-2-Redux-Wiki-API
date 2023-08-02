package com.github.blazeblack2reduxwikiapi.model.encounters;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "encounter_condition_values")
@Data
public class EncounterConditionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String value;
    String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    @JsonManagedReference
    EncounterCondition condition;

    @Override
    public String toString() {
        return "EncounterConditionValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
