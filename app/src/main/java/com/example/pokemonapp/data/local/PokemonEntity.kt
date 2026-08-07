package com.example.pokemonapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_pokemon")
data class PokemonEntity (
    @PrimaryKey
    val id:Int,
    val name: String,
    val imageUrl:String
)
