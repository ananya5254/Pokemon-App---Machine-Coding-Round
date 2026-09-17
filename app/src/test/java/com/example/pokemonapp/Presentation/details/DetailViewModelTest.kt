package com.example.pokemonapp.presentation.details

import com.example.pokemonapp.MainDispatcherRule
import com.example.pokemonapp.data.local.PokemonEntity
import com.example.pokemonapp.data.repository.PokemonRepository
import com.example.pokemonapp.domain.model.PokemonDetailResponse
import com.example.pokemonapp.domain.model.Sprites
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var repository: PokemonRepository

    private lateinit var viewModel: DetailViewModel

    private val bulbasaur = PokemonDetailResponse(
        id = 1,
        name = "bulbasaur",
        sprites = Sprites(front_default = "https://example.com/bulbasaur.png"),
        types = emptyList(),
        stats = emptyList()
    )

    @Before
    fun setUp() {
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun `getPokemon stores the loaded pokemon and favourite status`() = runTest {
        whenever(repository.getPokemonDetail("bulbasaur")).thenReturn(bulbasaur)
        whenever(repository.isFavorite(1)).thenReturn(flowOf(false))

        viewModel.getPokemon("bulbasaur")
        advanceUntilIdle()

        assertEquals(bulbasaur, viewModel.uiState.value.pokemon)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isFavorite)
        verify(repository).getPokemonDetail("bulbasaur")
        verify(repository).isFavorite(1)
    }

    @Test
    fun `toggleFavourite adds the displayed pokemon when it is not a favourite`() = runTest {
        whenever(repository.getPokemonDetail("bulbasaur")).thenReturn(bulbasaur)
        whenever(repository.isFavorite(1)).thenReturn(flowOf(false))
        val expectedEntity = PokemonEntity(
            id = 1,
            name = "bulbasaur",
            imageUrl = "https://example.com/bulbasaur.png"
        )

        viewModel.getPokemon("bulbasaur")
        advanceUntilIdle()
        viewModel.toggleFavourite()
        advanceUntilIdle()

        verify(repository).addFavorite(expectedEntity)
        verify(repository, never()).removeFavorite(expectedEntity)
    }

    @Test
    fun `toggleFavourite removes the displayed pokemon when it is already a favourite`() = runTest {
        whenever(repository.getPokemonDetail("bulbasaur")).thenReturn(bulbasaur)
        whenever(repository.isFavorite(1)).thenReturn(flowOf(true))
        val expectedEntity = PokemonEntity(
            id = 1,
            name = "bulbasaur",
            imageUrl = "https://example.com/bulbasaur.png"
        )

        viewModel.getPokemon("bulbasaur")
        advanceUntilIdle()
        viewModel.toggleFavourite()
        advanceUntilIdle()

        verify(repository).removeFavorite(expectedEntity)
        verify(repository, never()).addFavorite(expectedEntity)
    }
}
