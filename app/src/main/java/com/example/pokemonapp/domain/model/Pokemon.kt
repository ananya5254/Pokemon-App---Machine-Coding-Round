package com.example.pokemonapp.domain.model

data class Pokemon(
    val name: String,
    val url: String
) {

    val id: String
        get() = url.trimEnd('/').substringAfterLast('/')

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}