package org.efradev.todolist.domain

import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.repository.ShoppingListRepository

/**
 * Use case for getting shopping list details by ID
 * 
 * This use case handles the business logic for retrieving a specific shopping list
 * with all its items from the repository.
 */
class GetShoppingListDetailsUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    
    /**
     * Get shopping list details by ID
     * 
     * @param listId The ID of the shopping list to retrieve
     * @return Result containing the shopping list or error
     */
    suspend operator fun invoke(listId: String): Result<DomainShoppingList> {
        return try {
            if (listId.isBlank()) {
                Result.failure(IllegalArgumentException("List ID cannot be empty"))
            } else {
                shoppingListRepository.getShoppingListDetails(listId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
