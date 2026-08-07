package com.example.pokemonapp.domain.model


data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatSlot>
)