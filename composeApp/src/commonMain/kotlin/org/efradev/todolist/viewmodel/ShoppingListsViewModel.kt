package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.GetShoppingListsUseCase

sealed interface ShoppingListsUiState {
    data object Loading : ShoppingListsUiState
    data object Empty : ShoppingListsUiState
    data class Error(val message: String) : ShoppingListsUiState
    data class Success(val lists: List<DomainShoppingList>) : ShoppingListsUiState
}

class ShoppingListsViewModel(
    private val getShoppingListsUseCase: GetShoppingListsUseCase
) : ViewModel() {

    var uiState: ShoppingListsUiState by mutableStateOf(ShoppingListsUiState.Loading)
        private set

    init {
        loadShoppingLists()
    }

    fun loadShoppingLists() {
        viewModelScope.launch {
            uiState = ShoppingListsUiState.Loading
            getShoppingListsUseCase()
                .onSuccess { lists ->
                    uiState = if (lists.isEmpty()) {
                        ShoppingListsUiState.Empty
                    } else {
                        ShoppingListsUiState.Success(lists)
                    }
                }
                .onFailure { exception ->
                    uiState = ShoppingListsUiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}
