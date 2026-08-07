package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.local.PokemonDao
import com.example.pokemonapp.data.local.PokemonEntity
import com.example.pokemonapp.data.remote.api.PokemonService
import com.example.pokemonapp.domain.model.PokemonDetailResponse
import com.example.pokemonapp.domain.model.PokemonListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonService,   private val dao: PokemonDao): PokemonRepository {
    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): PokemonListResponse {
       return api.getPokemonList(limit,offset)
    }

    override suspend fun getPokemonDetail(name: String): PokemonDetailResponse {
       return api.getPokemonDetails(name)
    }
    override suspend fun addFavorite(
        pokemon: PokemonEntity
    ) {
        dao.insertPokemon(pokemon)
    }

    override suspend fun removeFavorite(
        pokemon: PokemonEntity
    ) {
        dao.deletePokemon(pokemon)
    }

    override fun getFavorites(): Flow<List<PokemonEntity>> {
        return dao.getFavoritePokemon()
    }

    override fun isFavorite(
        id: Int
    ): Flow<Boolean> {
        return dao.isFavorite(id)
    }

}