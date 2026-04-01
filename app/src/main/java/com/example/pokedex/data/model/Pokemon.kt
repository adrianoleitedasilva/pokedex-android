package com.example.pokedex.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListItem(
    val name: String,
    val url: String
)

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
)

data class TypeName(
    val name: String,
    val url: String
)

data class PokemonType(
    val slot: Int,
    val type: TypeName
)

data class StatName(
    val name: String,
    val url: String
)

data class PokemonStat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatName
)

data class AbilityName(
    val name: String,
    val url: String
)

data class PokemonAbility(
    val ability: AbilityName,
    val is_hidden: Boolean,
    val slot: Int
)

data class OfficialArtwork(
    val front_default: String?,
    val front_shiny: String?
)

data class DreamWorld(
    val front_default: String?
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork?,
    @SerializedName("dream_world") val dreamWorld: DreamWorld?
)

data class PokemonSprites(
    val front_default: String?,
    val front_shiny: String?,
    val other: OtherSprites?
)

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int?,
    val types: List<PokemonType>,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>,
    val sprites: PokemonSprites
)

data class Language(val name: String)

data class VersionName(val name: String)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language,
    val version: VersionName
)

data class Genus(
    val genus: String,
    val language: Language
)

data class SpeciesColor(val name: String)

data class PokemonSpecies(
    val flavor_text_entries: List<FlavorTextEntry>,
    val genera: List<Genus>,
    val color: SpeciesColor
)

data class TypePokemonRef(
    val name: String,
    val url: String
)

data class TypePokemonEntry(
    val pokemon: TypePokemonRef
)

data class TypeResponse(
    val pokemon: List<TypePokemonEntry>
)

data class TypeListItem(
    val name: String,
    val url: String
)

data class TypeListResponse(
    val results: List<TypeListItem>
)
