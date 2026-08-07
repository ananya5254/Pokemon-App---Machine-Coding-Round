package com.example.pokemonapp.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonapp.data.local.PokemonDao
import com.example.pokemonapp.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_db"
        ).build()
    }
    @Provides
    @Singleton
    fun providePokemonDao(
        database: PokemonDatabase
    ): PokemonDao{
        return database.pokemonDao()
    }
}