package com.example.pokemonapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.presentation.components.PokemonItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    onPokemonClick: (Pokemon) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.loadPokemon()
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.map {
            lastVisibleItem ->
            lastVisibleItem == uiState.pokemonList.lastIndex && uiState.searchText.isBlank()
        }
            .distinctUntilChanged()
            .filter{it}
            .collect{
                viewModel.loadPokemon()
            }



    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp)
        ) {

            OutlinedTextField(
                value = uiState.searchText,
                onValueChange = {
                    viewModel.onSearchTextChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Search Pokémon")
                },
                singleLine = true
            )
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.error != null -> {
                Text(text = uiState.error ?: "")
            }

            else -> {
                    LazyColumn(state = listState) {
                        items(uiState.pokemonList) { pokemon ->
                            PokemonItem(
                                pokemon = pokemon,
                                onClick = {
                                    onPokemonClick(it)
                                }
                            )
                        }
                        if (uiState.isLoadingMore) {

                            item {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}