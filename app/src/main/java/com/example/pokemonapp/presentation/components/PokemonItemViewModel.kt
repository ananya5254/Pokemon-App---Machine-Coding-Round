package com.example.pokemonapp.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonItemViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    fun loadImage(name: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonDetail(name)
                _imageUrl.value = response.sprites.front_default
            } catch (e: Exception) {
            }
        }
    }
}