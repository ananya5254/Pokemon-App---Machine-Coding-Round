package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.local.PokemonEntity
import com.example.pokemonapp.domain.model.PokemonDetailResponse
import com.example.pokemonapp.domain.model.PokemonListResponse
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse
    suspend fun getPokemonDetail(name: String): PokemonDetailResponse
    suspend fun addFavorite(
        pokemon: PokemonEntity
    )

    suspend fun removeFavorite(
        pokemon: PokemonEntity
    )

    fun getFavorites(): Flow<List<PokemonEntity>>

    fun isFavorite(
        id: Int
    ): Flow<Boolean>
}