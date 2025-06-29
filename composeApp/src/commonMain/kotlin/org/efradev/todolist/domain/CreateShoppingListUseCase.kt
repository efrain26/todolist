package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList

sealed interface CreateShoppingListResult {
    data class Success(val list: DomainShoppingList) : CreateShoppingListResult
    data class Error(val message: String) : CreateShoppingListResult
}

class CreateShoppingListUseCase(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(
        name: String,
        type: String = "simple"
    ): Result<CreateShoppingListResult> {
        return try {
            if (name.isBlank()) {
                Result.success(
                    CreateShoppingListResult.Error("El nombre de la lista es requerido")
                )
            } else {
                val result = repository.createList(
                    name = name.trim(),
                    type = type
                )
                
                result.fold(
                    onSuccess = { list ->
                        Result.success(CreateShoppingListResult.Success(list))
                    },
                    onFailure = { error ->
                        Result.success(
                            CreateShoppingListResult.Error(
                                error.message ?: "Error al crear la lista"
                            )
                        )
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
