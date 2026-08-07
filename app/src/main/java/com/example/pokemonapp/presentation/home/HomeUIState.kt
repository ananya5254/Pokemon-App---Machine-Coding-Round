package com.example.pokemonapp.presentation.home

import com.example.pokemonapp.domain.model.Pokemon

data class HomeUiState(
    val isLoading: Boolean = false,
    val pokemonList: List<Pokemon> = emptyList(),
    val error: String? = null,
    val isLoadingMore: Boolean = false,
    val searchText: String = ""
)