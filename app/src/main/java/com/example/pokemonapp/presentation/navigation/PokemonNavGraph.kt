package com.example.pokemonapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapp.presentation.details.DetailScreen
import com.example.pokemonapp.presentation.home.HomeScreen

@Composable
fun PokemonNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomeScreen(
                onPokemonClick = { pokemon ->

                    navController.navigate("detail/${pokemon.name}")

                }
            )
        }

        composable(
            route = "detail/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {

            val name = it.arguments?.getString("name") ?: ""

            DetailScreen(
                pokemonName = name
            )
        }
    }
}