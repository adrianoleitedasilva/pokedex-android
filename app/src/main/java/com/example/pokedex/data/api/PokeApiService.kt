package com.example.pokedex.data.api

import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.model.PokemonListResponse
import com.example.pokedex.data.model.PokemonSpecies
import com.example.pokedex.data.model.TypeListResponse
import com.example.pokedex.data.model.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 24,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemon(@Path("nameOrId") nameOrId: String): Pokemon

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Pokemon

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpecies

    @GET("type")
    suspend fun getAllTypes(@Query("limit") limit: Int = 20): TypeListResponse

    @GET("type/{type}")
    suspend fun getPokemonByType(@Path("type") type: String): TypeResponse
}
