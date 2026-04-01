package com.example.pokedex.ui.theme

import androidx.compose.ui.graphics.Color

val typeColors: Map<String, Color> = mapOf(
    "normal"   to Color(0xFFA8A77A),
    "fire"     to Color(0xFFEE8130),
    "water"    to Color(0xFF6390F0),
    "electric" to Color(0xFFF7D02C),
    "grass"    to Color(0xFF7AC74C),
    "ice"      to Color(0xFF96D9D6),
    "fighting" to Color(0xFFC22E28),
    "poison"   to Color(0xFFA33EA1),
    "ground"   to Color(0xFFE2BF65),
    "flying"   to Color(0xFFA98FF3),
    "psychic"  to Color(0xFFF95587),
    "bug"      to Color(0xFFA6B91A),
    "rock"     to Color(0xFFB6A136),
    "ghost"    to Color(0xFF735797),
    "dragon"   to Color(0xFF6F35FC),
    "dark"     to Color(0xFF705746),
    "steel"    to Color(0xFFB7B7CE),
    "fairy"    to Color(0xFFD685AD),
)

fun getTypeColor(typeName: String): Color = typeColors[typeName] ?: Color(0xFFA8A77A)

val statColors: Map<String, Color> = mapOf(
    "hp"               to Color(0xFFFF5959),
    "attack"           to Color(0xFFF5AC78),
    "defense"          to Color(0xFFFAE078),
    "special-attack"   to Color(0xFF9DB7F5),
    "special-defense"  to Color(0xFFA7DB8D),
    "speed"            to Color(0xFFFA92B2),
)

fun getStatColor(statName: String): Color = statColors[statName] ?: Color(0xFF9CA3AF)

fun getStatLabel(statName: String): String = when (statName) {
    "hp"              -> "HP"
    "attack"          -> "ATK"
    "defense"         -> "DEF"
    "special-attack"  -> "Sp.ATK"
    "special-defense" -> "Sp.DEF"
    "speed"           -> "SPD"
    else              -> statName
}
