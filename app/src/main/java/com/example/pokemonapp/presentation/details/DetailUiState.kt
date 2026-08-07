package com.example.pokemonapp.presentation.details

import com.example.pokemonapp.domain.model.PokemonDetailResponse

data class DetailUiState(
    val isLoading: Boolean = false,
    val pokemon: PokemonDetailResponse? = null,
    val error: String? = null,
    val isFavorite: Boolean = false
)