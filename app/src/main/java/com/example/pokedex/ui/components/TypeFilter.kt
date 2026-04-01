package com.example.pokedex.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.getTypeColor

@Composable
fun TypeFilter(
    types: List<String>,
    activeType: String?,
    onTypeSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (types.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = activeType == null,
            onClick = { onTypeSelected(null) },
            label = { Text("Todos", fontSize = 13.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White
            )
        )

        types.forEach { typeName ->
            val typeColor = getTypeColor(typeName)
            FilterChip(
                selected = activeType == typeName,
                onClick = { onTypeSelected(if (activeType == typeName) null else typeName) },
                label = { Text(typeName.replaceFirstChar { it.uppercase() }, fontSize = 13.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = typeColor,
                    selectedLabelColor = Color.White,
                    containerColor = typeColor.copy(alpha = 0.15f),
                    labelColor = typeColor
                )
            )
        }
    }
}
