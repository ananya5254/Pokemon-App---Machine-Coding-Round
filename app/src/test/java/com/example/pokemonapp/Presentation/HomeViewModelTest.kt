package com.example.pokemonapp.Presentation

import com.example.pokemonapp.MainDispatcherRule
import com.example.pokemonapp.data.repository.PokemonRepository
import com.example.pokemonapp.domain.model.PokemonListResponse
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.model.PokemonDetailResponse
import com.example.pokemonapp.domain.model.Sprites
import com.example.pokemonapp.presentation.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var repository: PokemonRepository

    lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(repository)
    }

    private val fakeResponse = PokemonListResponse(
        count = 1,
        next = null,
        previous = null,
        results = listOf(
            Pokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/"
            )
        )
    )
    private val fakeDetailResponse = PokemonDetailResponse(
        id = 1,
        name = "bulbasaur",
        sprites = Sprites(
            front_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        ),
        types = emptyList(),
        stats = emptyList()
    )
    @Test
    fun loadPokemon_success() = runTest {

        // Arrange
        whenever(repository.getPokemonList(20, 0))
            .thenReturn(fakeResponse)

        // Act
        viewModel.loadPokemon()

        advanceUntilIdle()

        // Assert
        verify(repository,times(2)).getPokemonList(20, 0)

        assertEquals(
            "bulbasaur",
            viewModel.uiState.value.pokemonList.first().name
        )

        assertFalse(viewModel.uiState.value.isLoading)
    }
    @Test
    fun loadPokemon_failure() = runTest {

        // Arrange
        whenever(repository.getPokemonList(20, 0))
            .thenThrow(RuntimeException("Network Error"))

        // Act
        viewModel.loadPokemon()

        advanceUntilIdle()

        // Assert
        verify(repository, times(2))
            .getPokemonList(20, 0)

        assertEquals(
            "Network Error",
            viewModel.uiState.value.error
        )

        assertFalse(viewModel.uiState.value.isLoading)
    }
    @Test
    fun searchPokemon_success() = runTest {
        // Arrange
        whenever(repository.getPokemonDetail("bulbasaur"))
            .thenReturn(fakeDetailResponse)

        // Let the ViewModel's initial blank-search collection finish before
        // exercising a direct search in this test.
        advanceUntilIdle()

        // Act
        viewModel.searchPokemon("bulbasaur")

        // Assert
        advanceUntilIdle()
        assertEquals(
            "bulbasaur",
            viewModel.uiState.value.pokemonList.first().name
        )

        assertFalse(viewModel.uiState.value.isLoading)

    }
}
