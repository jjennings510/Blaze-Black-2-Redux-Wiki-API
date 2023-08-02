package com.github.blazeblack2reduxwikiapi.model.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "base_stats")
@Getter
@Setter
@NoArgsConstructor
public class BaseStats {
    @Id
    @Column(name = "pokemon_id")
    private Long id;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int bst;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pokemon_id")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Pokemon pokemon;

    public void setBst() {
        this.bst = this.hp + this.attack + this.defense + this.specialAttack + this.specialDefense + this.speed;
    }

    @Override
    public String toString() {
        return "BaseStats{" +
                "id=" + id +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", specialAttack=" + specialAttack +
                ", specialDefense=" + specialDefense +
                ", speed=" + speed +
                ", bst=" + bst +
                '}';
    }
}
