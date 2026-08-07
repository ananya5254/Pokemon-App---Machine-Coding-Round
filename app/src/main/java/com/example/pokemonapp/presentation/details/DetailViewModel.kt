package com.example.pokemonapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.local.PokemonEntity
import com.example.pokemonapp.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {
    private val _uiState= MutableStateFlow(DetailUiState())
    val uiState =_uiState.asStateFlow()
    fun getPokemon(name:String){
        viewModelScope.launch() {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            try{
                val response=repository.getPokemonDetail(name)
                observeFavourite(response.id)
                _uiState.value=_uiState.value.copy(
                    isLoading = false,
                    pokemon = response
                )

            }catch (e: Exception){
                _uiState.value=_uiState.value.copy(
                    isLoading = true,
                    error = e.message
                )
            }
        }
    }
    fun observeFavourite(id:Int){
        viewModelScope.launch {
            repository.isFavorite(id).collect {
                favorite->
                _uiState.value=_uiState.value.copy(
                    isFavorite = favorite
                )
            }
        }
    }
    fun toggleFavourite(){
        val pokemon = _uiState.value.pokemon?:return
        val entity= PokemonEntity(
            id=pokemon.id,
            name=pokemon.name,
            imageUrl = pokemon.sprites.front_default ?: ""
        )
        viewModelScope.launch {
            if (_uiState.value.isFavorite){
                repository.removeFavorite(entity)
            }
            else{
                repository.addFavorite(entity)
            }
        }
    }

}