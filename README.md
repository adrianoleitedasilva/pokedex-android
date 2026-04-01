# Pokedex Android

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Android Studio](https://img.shields.io/badge/android%20studio-346ac1?style=for-the-badge&logo=android%20studio&logoColor=white) 

Aplicativo Android de Pokedex desenvolvido com Jetpack Compose, consumindo a [PokeAPI](https://pokeapi.co/) para exibir informações detalhadas sobre os Pokémon.

## Funcionalidades

- **Listagem de Pokémon** — grid de 2 colunas com 24 Pokémon por página
- **Busca** — pesquisa por nome ou número da Pokédex
- **Filtro por tipo** — 18 tipos disponíveis em chips horizontais
- **Paginação** — navegação entre páginas com seleção direta de página
- **Detalhes do Pokémon** — modal com:
  - Imagem padrão e shiny (✨)
  - Descrição em português (com fallback para inglês)
  - Classificação (gênero/espécie)
  - Altura, peso e XP base
  - Habilidades (incluindo habilidades ocultas)
  - Estatísticas base com barras de progresso coloridas

## Tecnologias

| Categoria | Biblioteca |
|---|---|
| UI | Jetpack Compose + Material Design 3 |
| Arquitetura | MVVM + StateFlow |
| Rede | Retrofit 2 + OkHttp 3 |
| Serialização | Gson |
| Imagens | Coil 2 |
| Assíncrono | Kotlin Coroutines |

## Arquitetura

O projeto segue o padrão **MVVM** com fluxo de dados unidirecional:

```
data/
├── api/
│   ├── PokeApiService.kt    # Interface Retrofit
│   └── RetrofitInstance.kt  # Singleton do cliente HTTP
└── model/
    └── Pokemon.kt           # Data classes dos responses

ui/
├── components/
│   ├── PokemonCard.kt       # Card da listagem
│   ├── PokemonDetail.kt     # Modal de detalhes
│   ├── SearchBar.kt         # Barra de busca
│   └── TypeFilter.kt        # Filtro de tipos
├── screen/
│   └── PokemonListScreen.kt # Tela principal
└── theme/
    ├── Color.kt
    ├── Theme.kt
    └── TypeColors.kt        # Cores por tipo de Pokémon

viewmodel/
└── PokemonViewModel.kt      # Estado e lógica de UI
```

## Requisitos

- Android Studio Hedgehog ou superior
- Android SDK 34
- Kotlin 1.9.23
- Dispositivo/emulador com Android 7.0+ (API 24)

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/pokedex-android.git
   ```

2. Abra o projeto no Android Studio.

3. Sincronize as dependências do Gradle.

4. Execute no emulador ou dispositivo físico.

## API

Este projeto utiliza a [PokeAPI v2](https://pokeapi.co/api/v2/) — uma API pública e gratuita. Não é necessária autenticação.

Endpoints utilizados:
- `GET /pokemon` — listagem com paginação
- `GET /pokemon/{id}` — detalhes do Pokémon
- `GET /pokemon-species/{id}` — descrição e espécie
- `GET /type` — lista de tipos
- `GET /type/{type}` — Pokémon por tipo
