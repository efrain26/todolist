package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.CreateShoppingListResult
import org.efradev.todolist.domain.CreateShoppingListUseCase
import org.efradev.todolist.data.model.ShoppingList

sealed interface CreateListUiState {
    data object Idle : CreateListUiState
    data object Loading : CreateListUiState
    data class Success(val list: ShoppingList) : CreateListUiState
    data class Error(val message: String) : CreateListUiState
}

class CreateListViewModel(
    private val createShoppingListUseCase: CreateShoppingListUseCase
) : ViewModel() {

    var uiState: CreateListUiState by mutableStateOf(CreateListUiState.Idle)
        private set

    fun createList(name: String, type: String = "simple") {
        if (name.isBlank()) {
            uiState = CreateListUiState.Error("El nombre de la lista es requerido")
            return
        }

        uiState = CreateListUiState.Loading
        viewModelScope.launch {
            createShoppingListUseCase(
                name = name.trim(),
                type = type
            ).fold(
                onSuccess = { result ->
                    when (result) {
                        is CreateShoppingListResult.Success -> {
                            uiState = CreateListUiState.Success(result.list)
                        }
                        is CreateShoppingListResult.Error -> {
                            uiState = CreateListUiState.Error(result.message)
                        }
                    }
                },
                onFailure = { error ->
                    uiState = CreateListUiState.Error(
                        error.message ?: "Error desconocido al crear la lista"
                    )
                }
            )
        }
    }

    fun resetState() {
        uiState = CreateListUiState.Idle
    }
}
