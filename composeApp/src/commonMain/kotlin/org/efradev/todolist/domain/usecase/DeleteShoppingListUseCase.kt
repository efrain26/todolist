package org.efradev.todolist.domain.usecase

import org.efradev.todolist.domain.repository.ShoppingListRepository

/**
 * Use case for deleting a shopping list
 * 
 * This use case handles the business logic for deleting a shopping list.
 * Follows Clean Architecture principles by encapsulating business rules.
 */
class DeleteShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    
    /**
     * Delete a shopping list by ID
     * 
     * @param listId The ID of the shopping list to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(listId: String): Result<Unit> {
        return try {
            if (listId.isBlank()) {
                Result.failure(IllegalArgumentException("List ID cannot be empty"))
            } else {
                shoppingListRepository.deleteList(listId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
