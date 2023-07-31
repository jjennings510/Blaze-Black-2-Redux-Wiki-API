package com.github.blazeblack2reduxwikiapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Entity
@Table(name = "types")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @ManyToMany(mappedBy = "types")
    @JsonBackReference
    private List<Pokemon> pokemonList;
    @OneToMany(mappedBy = "type")
    private List<Move> moves;

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
