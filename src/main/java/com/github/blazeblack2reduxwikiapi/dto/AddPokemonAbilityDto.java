package com.github.blazeblack2reduxwikiapi.dto;
public class AddPokemonAbilityDto {
    private Long id;
    private boolean isHiddenAbility;
    private Long abilityId;
    private Long pokemonId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHiddenAbility() {
        return isHiddenAbility;
    }

    public void setIsHiddenAbility(String hiddenAbility) {
        isHiddenAbility = hiddenAbility.equals("1");
    }

    public Long getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(Long abilityId) {
        this.abilityId = abilityId;
    }

    public Long getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Long pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Override
    public String toString() {
        return "AddPokemonAbilityDto{" +
                "id=" + id +
                ", isHiddenAbility=" + isHiddenAbility +
                ", abilityId=" + abilityId +
                ", pokemonId=" + pokemonId +
                '}';
    }
}
