package com.example.pokemonapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.repository.PokemonRepository
import com.example.pokemonapp.domain.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Locale.getDefault

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PokemonRepository): ViewModel() {
    private val _uiState=MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val searchQuery = MutableStateFlow("")
    init{
        observeSearch()
    }

    private var offset = 0
    private val limit = 20
    private var isRequestRunning = false
    private var isLastPage = false

    fun loadPokemon() {

        if (isRequestRunning || isLastPage) return

        isRequestRunning = true

        viewModelScope.launch {

            if (offset == 0) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            } else {
                _uiState.value = _uiState.value.copy(isLoadingMore = true)
            }

            try {

                val response = repository.getPokemonList(limit, offset)

                if (response.results.isEmpty()) {

                    isLastPage = true

                } else {

                    offset += limit

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        pokemonList = _uiState.value.pokemonList + response.results
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = e.message
                )
            }

            isRequestRunning = false
        }
    }
    fun onSearchTextChanged(text: String) {

        _uiState.value = _uiState.value.copy(
            searchText = text
        )

        searchQuery.value = text
    }
    fun searchPokemon(name:String){
        if(name.isBlank())
            return
        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(
               isLoading = true
            )
            try {
                val response=repository.getPokemonDetail(name.lowercase(getDefault()))
                _uiState.value=_uiState.value.copy(
                    isLoading = false,
                    pokemonList = listOf(
                        Pokemon(
                            name= response.name,
                            url = "https://pokeapi.co/api/v2/pokemon/${response.id}/"
                        )
                    ),
                    error = null
                )
            } catch (e: Exception){
                _uiState.value=_uiState.value.copy(
                    isLoading = false,
                    pokemonList = emptyList(),
                    error="No Pokemon found"
                )

            }
        }
    }
    private fun observeSearch() {

        viewModelScope.launch {

            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->

                    if (query.isBlank()) {

                        offset = 0
                        isLastPage = false

                        _uiState.value = _uiState.value.copy(
                            pokemonList = emptyList(),
                            error = null
                        )

                        loadPokemon()

                    } else {

                        searchPokemon(query)
                    }
                }
        }
    }

}