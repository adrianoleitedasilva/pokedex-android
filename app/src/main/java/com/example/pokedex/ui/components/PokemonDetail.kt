package com.example.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.model.PokemonSpecies
import com.example.pokedex.ui.theme.getStatColor
import com.example.pokedex.ui.theme.getStatLabel
import com.example.pokedex.ui.theme.getTypeColor

@Composable
fun PokemonDetail(
    pokemon: Pokemon,
    species: PokemonSpecies?,
    onClose: () -> Unit
) {
    val primaryColor = getTypeColor(pokemon.types.firstOrNull()?.type?.name ?: "normal")
    var shiny by remember { mutableStateOf(false) }

    val imageUrl = if (shiny) {
        pokemon.sprites.other?.officialArtwork?.front_shiny
            ?: pokemon.sprites.front_shiny
            ?: pokemon.sprites.other?.officialArtwork?.front_default
            ?: pokemon.sprites.front_default ?: ""
    } else {
        pokemon.sprites.other?.officialArtwork?.front_default
            ?: pokemon.sprites.front_default ?: ""
    }

    val descriptionText = (
        species?.flavor_text_entries?.find { it.language.name == "pt-BR" }
            ?: species?.flavor_text_entries?.find { it.language.name == "pt" }
            ?: species?.flavor_text_entries?.find { it.language.name == "en" }
    )?.flavor_text?.replace("\n", " ")?.replace("\u000c", " ") ?: ""

    val genus = species?.genera?.find { it.language.name == "pt-BR" }?.genus
        ?: species?.genera?.find { it.language.name == "pt" }?.genus
        ?: species?.genera?.find { it.language.name == "en" }?.genus
        ?: ""

    val formattedId = "#${pokemon.id.toString().padStart(4, '0')}"
    val totalStats = pokemon.stats.sumOf { it.base_stat }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(primaryColor)
                            .padding(16.dp)
                    ) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = formattedId,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (genus.isNotEmpty()) {
                                    Text(
                                        text = genus,
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    pokemon.types.forEach { pokemonType ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50.dp))
                                                .background(Color.White.copy(alpha = 0.3f))
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = pokemonType.type.name.replaceFirstChar { it.uppercase() },
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                IconButton(
                                    onClick = { shiny = !shiny },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(
                                            if (shiny) Color.White.copy(alpha = 0.35f)
                                            else Color.White.copy(alpha = 0.15f)
                                        )
                                ) {
                                    Text("✨", fontSize = 18.sp)
                                }
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = pokemon.name,
                                    modifier = Modifier.size(120.dp)
                                )
                            }
                        }
                    }

                    // Body
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (descriptionText.isNotEmpty()) {
                            Text(
                                text = descriptionText,
                                fontSize = 14.sp,
                                color = Color(0xFF374151),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Info grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoItem(
                                label = "Altura",
                                value = "${"%.1f".format(pokemon.height / 10.0)} m",
                                modifier = Modifier.weight(1f)
                            )
                            InfoItem(
                                label = "Peso",
                                value = "${"%.1f".format(pokemon.weight / 10.0)} kg",
                                modifier = Modifier.weight(1f)
                            )
                            InfoItem(
                                label = "XP Base",
                                value = pokemon.base_experience?.toString() ?: "—",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Abilities
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Habilidades",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B7280)
                            )
                            pokemon.abilities.forEach { abilityEntry ->
                                val name = abilityEntry.ability.name
                                    .replace("-", " ")
                                    .replaceFirstChar { it.uppercase() }
                                Text(
                                    text = if (abilityEntry.is_hidden) "$name (oculta)" else name,
                                    fontSize = 14.sp,
                                    color = if (abilityEntry.is_hidden) Color(0xFF9CA3AF) else Color(0xFF1F2937)
                                )
                            }
                        }

                        // Stats
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Estatísticas Base",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1F2937)
                            )
                            pokemon.stats.forEach { stat ->
                                StatRow(
                                    label = getStatLabel(stat.stat.name),
                                    value = stat.base_stat,
                                    color = getStatColor(stat.stat.name),
                                    percent = minOf(100, stat.base_stat * 100 / 255)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Total",
                                    modifier = Modifier.width(72.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF374151)
                                )
                                Text(
                                    totalStats.toString(),
                                    modifier = Modifier.width(40.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1F2937),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9FAFB))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 11.sp, color = Color(0xFF6B7280))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color, percent: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(72.dp),
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value.toString(),
            modifier = Modifier.width(36.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percent / 100f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(color)
            )
        }
    }
}
