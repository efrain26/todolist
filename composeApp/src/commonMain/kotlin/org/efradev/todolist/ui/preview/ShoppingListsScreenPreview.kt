package org.efradev.todolist.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.efradev.todolist.ui.screens.ShoppingListsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ShoppingListsScreenPreview() {
    MaterialTheme {
        ShoppingListsScreen(
            onLogout = {}
        )
    }
}

@Preview
@Composable
fun ShoppingListsScreenLogoutPreview() {
    MaterialTheme {
        ShoppingListsScreen(
            onLogout = {}
        )
    }
}
