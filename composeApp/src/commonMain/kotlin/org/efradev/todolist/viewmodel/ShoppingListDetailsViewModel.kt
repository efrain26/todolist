package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.GetShoppingListDetailsUseCase
import org.efradev.todolist.domain.model.DomainShoppingList

/**
 * UI State for Shopping List Details Screen
 * 
 * Represents all possible states of the list details screen,
 * following the established UI state pattern.
 */
sealed class ShoppingListDetailsUiState {
    data object Loading : ShoppingListDetailsUiState()
    data class Success(val list: DomainShoppingList) : ShoppingListDetailsUiState()
    data class Error(val message: String) : ShoppingListDetailsUiState()
}

/**
 * ViewModel for Shopping List Details Screen
 * 
 * Manages the state and business logic for displaying shopping list details.
 * Follows Clean Architecture principles by using domain use cases.
 */
class ShoppingListDetailsViewModel(
    private val getShoppingListDetailsUseCase: GetShoppingListDetailsUseCase
) : ViewModel() {

    var uiState by mutableStateOf<ShoppingListDetailsUiState>(ShoppingListDetailsUiState.Loading)
        private set

    /**
     * Load shopping list details by ID
     * 
     * @param listId The ID of the list to load
     */
    fun loadListDetails(listId: String) {
        viewModelScope.launch {
            uiState = ShoppingListDetailsUiState.Loading
            
            getShoppingListDetailsUseCase(listId)
                .onSuccess { list ->
                    uiState = ShoppingListDetailsUiState.Success(list)
                }
                .onFailure { error ->
                    uiState = ShoppingListDetailsUiState.Error(
                        error.message ?: "Failed to load list details"
                    )
                }
        }
    }

    /**
     * Refresh the current list details
     * Used for pull-to-refresh functionality
     */
    fun refresh(listId: String) {
        loadListDetails(listId)
    }

    /**
     * Reset state to loading
     * Useful for navigation scenarios
     */
    fun resetState() {
        uiState = ShoppingListDetailsUiState.Loading
    }
}
