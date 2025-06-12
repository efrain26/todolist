package org.efradev.todolist.domain

import org.efradev.todolist.data.ShoppingListRepository
import org.efradev.todolist.data.model.ShoppingList

class GetShoppingListsUseCase(
    private val repository: ShoppingListRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(): Result<List<ShoppingList>> {
        return try {
            repository.getShoppingLists()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
