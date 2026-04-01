package com.example.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.ui.theme.getTypeColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = getTypeColor(pokemon.types.firstOrNull()?.type?.name ?: "normal")
    val formattedId = "#${pokemon.id.toString().padStart(4, '0')}"
    val imageUrl = pokemon.sprites.other?.officialArtwork?.front_default
        ?: pokemon.sprites.front_default
        ?: ""

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.25f), Color.White),
                        startY = 0f,
                        endY = 300f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formattedId,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.align(Alignment.End)
                )

                AsyncImage(
                    model = imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                )

                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    pokemon.types.forEach { pokemonType ->
                        TypeBadge(
                            typeName = pokemonType.type.name,
                            color = getTypeColor(pokemonType.type.name)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypeBadge(typeName: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = typeName.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
