package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.GetShoppingListDetailsUseCase
import org.efradev.todolist.domain.usecase.AddItemToListUseCase
import org.efradev.todolist.domain.usecase.DeleteShoppingListUseCase
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
    data object Deleted : ShoppingListDetailsUiState()
}

/**
 * ViewModel for Shopping List Details Screen
 * 
 * Manages the state and business logic for displaying shopping list details.
 * Follows Clean Architecture principles by using domain use cases.
 */
class ShoppingListDetailsViewModel(
    private val getShoppingListDetailsUseCase: GetShoppingListDetailsUseCase,
    private val addItemToListUseCase: AddItemToListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase
) : ViewModel() {

    var uiState by mutableStateOf<ShoppingListDetailsUiState>(ShoppingListDetailsUiState.Loading)
        private set
    
    var isAddingItem by mutableStateOf(false)
        private set

    var isDeletingList by mutableStateOf(false)
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
     * Add a new item to the shopping list
     * 
     * @param listId The ID of the list to add the item to
     * @param itemName The name of the item to add
     * @param listType The type of the list/item
     */
    fun addItem(listId: String, itemName: String, listType: String = "simple") {
        viewModelScope.launch {
            isAddingItem = true
            
            addItemToListUseCase(
                listId = listId,
                itemName = itemName,
                listType = listType
            )
                .onSuccess { updatedList ->
                    uiState = ShoppingListDetailsUiState.Success(updatedList)
                    isAddingItem = false
                }
                .onFailure { error ->
                    isAddingItem = false
                    // Keep current state and show error message
                    // Could be enhanced with a snackbar or toast notification
                    uiState = ShoppingListDetailsUiState.Error(
                        error.message ?: "Failed to add item"
                    )
                }
        }
    }

    /**
     * Delete the current shopping list
     * 
     * @param listId The ID of the list to delete
     */
    fun deleteList(listId: String) {
        viewModelScope.launch {
            isDeletingList = true
            
            deleteShoppingListUseCase(listId)
                .onSuccess {
                    isDeletingList = false
                    uiState = ShoppingListDetailsUiState.Deleted
                }
                .onFailure { error ->
                    isDeletingList = false
                    uiState = ShoppingListDetailsUiState.Error(
                        error.message ?: "Failed to delete list"
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
