package com.example.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.api.RetrofitInstance
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.model.PokemonSpecies
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

const val PAGE_SIZE = 24

data class PokemonListState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchError: String? = null,
    val currentPage: Int = 0,
    val totalCount: Int = 0,
    val isSearchMode: Boolean = false,
    val isTypeMode: Boolean = false,
    val activeType: String? = null,
    val selectedPokemon: Pokemon? = null,
    val selectedSpecies: PokemonSpecies? = null,
    val types: List<String> = emptyList()
) {
    val totalPages: Int
        get() = if (totalCount == 0) 1 else ceil(totalCount.toDouble() / PAGE_SIZE).toInt()

    val pages: List<Int>
        get() {
            val delta = 2
            val start = maxOf(0, currentPage - delta)
            val end = minOf(totalPages - 1, currentPage + delta)
            return (start..end).toList()
        }
}

class PokemonViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    init {
        loadPage(0)
        loadTypes()
    }

    fun loadPage(page: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isSearchMode = false,
                    isTypeMode = false,
                    activeType = null,
                    currentPage = page
                )
            }
            try {
                val offset = page * PAGE_SIZE
                val listResponse = api.getPokemonList(PAGE_SIZE, offset)
                val pokemons = listResponse.results
                    .map { item -> async { api.getPokemon(item.name) } }
                    .awaitAll()
                _state.update {
                    it.copy(pokemons = pokemons, isLoading = false, totalCount = listResponse.count)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = "Falha ao carregar os Pokémon. Tente novamente.")
                }
            }
        }
    }

    fun search(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, searchError = null, isSearchMode = true, isTypeMode = false, activeType = null)
            }
            try {
                val identifier = trimmed.toLongOrNull()?.toString() ?: trimmed.lowercase()
                val pokemon = api.getPokemon(identifier)
                _state.update { it.copy(pokemons = listOf(pokemon), isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        pokemons = emptyList(),
                        isLoading = false,
                        searchError = "Nenhum Pokémon encontrado para \"$trimmed\". Tente outro nome ou número."
                    )
                }
            }
        }
    }

    fun clearSearch() {
        _state.update { it.copy(isSearchMode = false, searchError = null) }
        loadPage(0)
    }

    fun filterByType(typeName: String?) {
        if (typeName == null) {
            _state.update { it.copy(isTypeMode = false, activeType = null, isSearchMode = false, searchError = null) }
            loadPage(0)
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null, isSearchMode = false, isTypeMode = true, activeType = typeName)
            }
            try {
                val typeResponse = api.getPokemonByType(typeName)
                val pokemons = typeResponse.pokemon
                    .take(PAGE_SIZE)
                    .map { entry ->
                        val id = entry.pokemon.url.trimEnd('/').split('/').last().toInt()
                        async { api.getPokemonById(id) }
                    }
                    .awaitAll()
                _state.update { it.copy(pokemons = pokemons, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = "Falha ao filtrar por tipo. Tente novamente.")
                }
            }
        }
    }

    fun selectPokemon(pokemon: Pokemon?) {
        _state.update { it.copy(selectedPokemon = pokemon, selectedSpecies = null) }
        if (pokemon != null) {
            viewModelScope.launch {
                try {
                    val species = api.getPokemonSpecies(pokemon.id)
                    _state.update { it.copy(selectedSpecies = species) }
                } catch (e: Exception) { /* silently ignore */ }
            }
        }
    }

    private fun loadTypes() {
        viewModelScope.launch {
            try {
                val response = api.getAllTypes()
                val valid = response.results
                    .map { it.name }
                    .filter { it != "unknown" && it != "shadow" }
                _state.update { it.copy(types = valid) }
            } catch (e: Exception) { /* silently ignore */ }
        }
    }

    fun nextPage() {
        val s = _state.value
        if (s.currentPage < s.totalPages - 1) loadPage(s.currentPage + 1)
    }

    fun prevPage() {
        val s = _state.value
        if (s.currentPage > 0) loadPage(s.currentPage - 1)
    }

    fun goToPage(page: Int) = loadPage(page)
}
