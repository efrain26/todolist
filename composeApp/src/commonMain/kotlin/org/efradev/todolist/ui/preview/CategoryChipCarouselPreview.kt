package org.efradev.todolist.ui.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.efradev.todolist.ui.components.CategoryChipCarousel
import org.efradev.todolist.ui.components.CategoryOption
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CategoryChipCarouselPreview() {
    val categories = listOf(
        CategoryOption(id = "all", label = "Todas", icon = Icons.Default.Star),
        CategoryOption(id = "groceries", label = "Comestibles", icon = Icons.Default.ShoppingCart),
        CategoryOption(id = "home", label = "Hogar", icon = Icons.Default.Home),
        CategoryOption(id = "personal", label = "Personal", icon = Icons.Default.Favorite)
    )

    MaterialTheme {
        CategoryChipCarousel(
            categories = categories,
            selectedCategory = "groceries",
            onCategorySelected = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun CategoryChipCarouselNoneSelectedPreview() {
    val categories = listOf(
        CategoryOption(id = "all", label = "Todas"),
        CategoryOption(id = "groceries", label = "Comestibles"),
        CategoryOption(id = "home", label = "Hogar"),
        CategoryOption(id = "personal", label = "Personal"),
        CategoryOption(id = "work", label = "Trabajo"),
        CategoryOption(id = "health", label = "Salud")
    )

    MaterialTheme {
        CategoryChipCarousel(
            categories = categories,
            selectedCategory = "",
            onCategorySelected = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun CategoryChipCarouselLongLabelsPreview() {
    val categories = listOf(
        CategoryOption(id = "all", label = "Todas las categorías"),
        CategoryOption(id = "groceries", label = "Comestibles y productos frescos"),
        CategoryOption(id = "home", label = "Artículos para el hogar"),
        CategoryOption(id = "personal", label = "Cuidado personal y belleza")
    )

    MaterialTheme {
        CategoryChipCarousel(
            categories = categories,
            selectedCategory = "groceries",
            onCategorySelected = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
