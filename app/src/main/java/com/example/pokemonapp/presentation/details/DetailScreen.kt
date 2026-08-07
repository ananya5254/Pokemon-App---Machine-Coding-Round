package com.example.pokemonapp.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder

@Composable
fun DetailScreen(pokemonName:String,viewModel: DetailViewModel= hiltViewModel()){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getPokemon(pokemonName)
    }
    when{
        uiState.isLoading ->
            CircularProgressIndicator()
         uiState.error != null ->{
             Box(
                 modifier = Modifier.fillMaxSize(),
                 contentAlignment = Alignment.Center
             ) {
                 Text(uiState.error!!)
             }
         }
        uiState.pokemon !=null ->{
            val pokemon=uiState.pokemon
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                item {
                    AsyncImage(
                        model = pokemon?.sprites?.front_default,
                        contentDescription = pokemon?.name,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    IconButton(onClick = {viewModel.toggleFavourite()}) {
                        Icon(
                            imageVector = if(uiState.isFavorite)
                                          Icons.Default.Star
                                          else
                                Icons.Default.StarBorder,
                            contentDescription = null
                        )
                    }
                    pokemon?.name?.let { Text(text=it)}
                    Text("Types")
                    pokemon?.types?.forEach {
                        Text(it.type.name)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Stats")
                    pokemon?.stats?.forEach {
                        Text("${it.stat.name} : ${it.base_stat}")
                    }
                }

            }


        }
    }

}

