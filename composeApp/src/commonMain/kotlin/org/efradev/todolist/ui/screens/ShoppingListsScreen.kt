package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.efradev.todolist.data.model.ShoppingList
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.viewmodel.ShoppingListsUiState
import org.efradev.todolist.viewmodel.ShoppingListsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen() {
    val viewModel: ShoppingListsViewModel = koinViewModel<ShoppingListsViewModel>()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = { },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = { /* TODO: Implement profile action */ }) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                // TODO: Replace with actual profile image
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxSize()
                                ) {}
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Implement add new list */ },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add new list"
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = { Text("Account") }
                )
            }
        }
    ) { paddingValues ->
        when (val state = viewModel.uiState) {
            is ShoppingListsUiState.Loading -> LoadingIndicator()
            is ShoppingListsUiState.Empty -> EmptyState()
            is ShoppingListsUiState.Error -> ErrorState(state.message)
            is ShoppingListsUiState.Success -> ShoppingListsContent(
                lists = state.lists,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No lists found.\nAdd a new list to get started!",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShoppingListsContent(
    lists: List<ShoppingList>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(lists) { list ->
            ShoppingListItem(list = list)
            Divider()
        }
    }
}

@Composable
private fun ShoppingListItem(list: ShoppingList) {
    ListItem(
        headlineContent = { 
            Text(
                text = list.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = "Items: ${list.itemsCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            // TODO: Replace with actual list image or icon
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {}
        },
        trailingContent = {
            IconButton(onClick = { /* TODO: Show menu */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview
@Composable
fun ShoppingListsScreenPreview() {
    initKoin()
    val mockShoppingLists = listOf(
        ShoppingList(
            id = "1",
            name = "Compras de la semana",
            itemsCount = 1
        ),
        ShoppingList(
            id = "2",
            name = "Lista de supermercado",
            itemsCount = 1
        ),
        ShoppingList(
            id = "1",
            name = "Compras de la semana",
            itemsCount = 1
        ),
        ShoppingList(
            id = "2",
            name = "Lista de supermercado",
            itemsCount = 1
        )
    )

    MaterialTheme {
        ShoppingListsContent(
            lists = mockShoppingLists,
            modifier = Modifier.fillMaxSize()
        )
//        ShoppingListsScreen()
    }
}
