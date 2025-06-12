package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.efradev.todolist.data.model.ShoppingList
import org.efradev.todolist.viewmodel.ShoppingListsUiState
import org.efradev.todolist.viewmodel.ShoppingListsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShoppingListsScreen(
) {

    val viewModel: ShoppingListsViewModel = koinViewModel<ShoppingListsViewModel>()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Implement add list */ }) {
//                Icon(Icons.Default.Add, contentDescription = "Add list")
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lists) { list ->
            ShoppingListCard(list = list)
        }
    }
}

@Composable
private fun ShoppingListCard(list: ShoppingList) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${list.itemsCount} items",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
