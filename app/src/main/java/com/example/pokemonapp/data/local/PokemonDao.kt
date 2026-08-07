package com.example.pokemonapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE


@Dao
interface PokemonDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(
        pokemon: PokemonEntity
    )
    @Delete
    suspend fun deletePokemon(
        pokemon: PokemonEntity
    )
    @Query("SELECT * FROM favourite_pokemon")
    fun getFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_pokemon WHERE id=:id)")
    fun isFavorite(
        id: Int
    ): Flow<Boolean>




}