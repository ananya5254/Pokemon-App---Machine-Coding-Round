package com.example.pokemonapp.data.remote.api
import com.example.pokemonapp.domain.model.PokemonDetailResponse
import com.example.pokemonapp.domain.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface PokemonService{
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit")limit: Int,
        @Query("offset")offset:Int
    ): PokemonListResponse
    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): PokemonDetailResponse


}
