package com.example.pokemonapp.di

import com.example.pokemonapp.data.remote.api.PokemonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL="https://pokeapi.co/api/v2/"
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply{
            level= HttpLoggingInterceptor.Level.BODY
        }
    @Provides
    @Singleton
    fun provideOkHTTPClient(
       loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient= OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    @Provides
    @Singleton
    fun providePokemonService(  retrofit:Retrofit): PokemonService=retrofit.create(PokemonService::class.java)


}