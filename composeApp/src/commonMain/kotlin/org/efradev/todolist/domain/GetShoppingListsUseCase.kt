package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList

class GetShoppingListsUseCase(
    private val repository: ShoppingListRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(): Result<List<DomainShoppingList>> {
        return try {
            repository.getShoppingLists()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
