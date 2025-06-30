package org.efradev.todolist.ui.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.ui.screens.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for Shopping List Details Screen Components
 * 
 * Following UI Pattern Guide: Separated preview files with multiple states
 * Each preview shows a different component or state of the screen
 */

// Sample data for previews
private val sampleItems = listOf(
    DomainShoppingItem(
        name = "Organic Bananas",
        status = "pendiente",
        type = "compras"
    ),
    DomainShoppingItem(
        name = "Greek Yogurt",
        status = "completado", 
        type = "compras"
    ),
    DomainShoppingItem(
        name = "Dark Chocolate",
        status = "en_progreso",
        type = "compras"
    )
)

private val sampleList = DomainShoppingList(
    id = "sample-list-123",
    name = "Weekly Groceries",
    createdAt = "2023-06-29T10:00:00Z",
    userId = "user-123",
    type = "compras",
    items = sampleItems
)

private val emptyList = DomainShoppingList(
    id = "empty-list-123",
    name = "Empty Shopping List",
    createdAt = "2023-06-29T10:00:00Z",
    userId = "user-123",
    type = "simple",
    items = emptyList()
)

// Full screen preview with Koin initialization (will show loading state)
@Preview
@Composable
fun ShoppingListDetailsScreenPreview() {
    MaterialTheme {
        try {
            initKoin()
        } catch (e: Exception) {
            // Koin already initialized
            e.printStackTrace()
        }
        ShoppingListDetailsScreen(
            listId = "preview-list-id",
            onBackClick = {}
        )
    }
}

// Individual component previews

@Preview
@Composable
fun HeaderSectionPreview() {
    MaterialTheme {
        HeaderSection(
            list = sampleList,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun DescriptionSectionPreview() {
    MaterialTheme {
        DescriptionSection(
            list = sampleList,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun ItemsSectionHeaderPreview() {
    MaterialTheme {
        ItemsSectionHeader(
            itemCount = 3,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun ListItemCardPreview() {
    MaterialTheme {
        ListItemCard(
            item = sampleItems[0],
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun ListItemCardCompletedPreview() {
    MaterialTheme {
        ListItemCard(
            item = sampleItems[1],
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun LoadingContentPreview() {
    MaterialTheme {
        LoadingContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun ErrorContentPreview() {
    MaterialTheme {
        ErrorContent(
            message = "Failed to load list details. Please check your internet connection.",
            onRetry = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun EmptyItemsStatePreview() {
    MaterialTheme {
        EmptyItemsState(
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Preview
@Composable
fun ListDetailsContentWithItemsPreview() {
    MaterialTheme {
        ListDetailsContent(
            list = sampleList,
            onRefresh = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun ListDetailsContentEmptyPreview() {
    MaterialTheme {
        ListDetailsContent(
            list = emptyList,
            onRefresh = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
