package com.example.pokedex.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.ui.components.PokemonCard
import com.example.pokedex.ui.components.PokemonDetail
import com.example.pokedex.ui.components.SearchBar
import com.example.pokedex.ui.components.TypeFilter
import com.example.pokedex.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(vm: PokemonViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⬤", color = Color(0xFFCC0000), fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pokédex", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1F2937)
                )
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                onSearch = vm::search,
                onClear = vm::clearSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            TypeFilter(
                types = state.types,
                activeType = state.activeType,
                onTypeSelected = vm::filterByType
            )

            when {
                state.isLoading -> LoadingGrid()

                state.error != null -> ErrorState(
                    message = state.error!!,
                    onRetry = { vm.loadPage(0) }
                )

                state.searchError != null -> EmptyState(
                    message = state.searchError!!,
                    onBack = vm::clearSearch
                )

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.pokemons, key = { it.id }) { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = { vm.selectPokemon(pokemon) }
                            )
                        }
                    }

                    if (!state.isSearchMode && !state.isTypeMode && state.totalPages > 1) {
                        Pagination(
                            currentPage = state.currentPage,
                            totalPages = state.totalPages,
                            pages = state.pages,
                            onPrev = vm::prevPage,
                            onNext = vm::nextPage,
                            onGoTo = vm::goToPage
                        )
                    }
                }
            }
        }
    }

    state.selectedPokemon?.let { pokemon ->
        PokemonDetail(
            pokemon = pokemon,
            species = state.selectedSpecies,
            onClose = { vm.selectPokemon(null) }
        )
    }
}

@Composable
private fun LoadingGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(12) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E7EB))
            ) {}
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(message, color = Color(0xFF6B7280), textAlign = TextAlign.Center)
            Button(onClick = onRetry) { Text("Tentar novamente") }
        }
    }
}

@Composable
private fun EmptyState(message: String, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text("⬤", fontSize = 48.sp, color = Color(0xFFD1D5DB))
            Text(message, color = Color(0xFF6B7280), textAlign = TextAlign.Center)
            OutlinedButton(onClick = onBack) { Text("Voltar à lista") }
        }
    }
}

@Composable
private fun Pagination(
    currentPage: Int,
    totalPages: Int,
    pages: List<Int>,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onGoTo: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PageNavButton(label = "‹", enabled = currentPage > 0, onClick = onPrev)

        if (pages.firstOrNull() ?: 0 > 0) {
            PageNumberButton(page = 0, isActive = currentPage == 0, onClick = onGoTo)
            if ((pages.firstOrNull() ?: 0) > 1) {
                Text("…", modifier = Modifier.padding(horizontal = 2.dp), color = Color(0xFF9CA3AF))
            }
        }

        pages.forEach { page ->
            PageNumberButton(page = page, isActive = page == currentPage, onClick = onGoTo)
        }

        val lastPage = pages.lastOrNull() ?: (totalPages - 1)
        if (lastPage < totalPages - 1) {
            if (lastPage < totalPages - 2) {
                Text("…", modifier = Modifier.padding(horizontal = 2.dp), color = Color(0xFF9CA3AF))
            }
            PageNumberButton(page = totalPages - 1, isActive = currentPage == totalPages - 1, onClick = onGoTo)
        }

        PageNavButton(label = "›", enabled = currentPage < totalPages - 1, onClick = onNext)
    }
}

@Composable
private fun PageNavButton(label: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF374151)),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
    ) {
        Text(label, fontSize = 22.sp, fontWeight = FontWeight.Light)
    }
}

@Composable
private fun PageNumberButton(page: Int, isActive: Boolean, onClick: (Int) -> Unit) {
    Button(
        onClick = { onClick(page) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFCC0000) else Color.White,
            contentColor = if (isActive) Color.White else Color(0xFF374151)
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        modifier = Modifier.defaultMinSize(minWidth = 36.dp, minHeight = 36.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isActive) 0.dp else 1.dp)
    ) {
        Text((page + 1).toString(), fontSize = 13.sp)
    }
}
