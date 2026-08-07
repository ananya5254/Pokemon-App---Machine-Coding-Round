package com.example.pokemonapp.di

import com.example.pokemonapp.data.repository.PokemonRepository
import com.example.pokemonapp.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        repositoryImpl: PokemonRepositoryImpl
    ): PokemonRepository

}