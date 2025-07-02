package org.efradev.todolist.domain.usecase

import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainAddItemRequest
import org.efradev.todolist.domain.repository.ShoppingListRepository

/**
 * Use case for adding a new item to a shopping list
 */
class AddItemToListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    
    /**
     * Adds a new item to the specified shopping list
     * 
     * @param listId The ID of the list to add the item to
     * @param itemName The name of the item to add
     * @param listType The type of list (simple, compras, etc.)
     * @param additionalFields Optional additional fields based on list type
     * @return Result containing the updated shopping list or error
     */
    suspend operator fun invoke(
        listId: String,
        itemName: String,
        listType: String = "simple",
        price: Double? = null,
        quantity: Int? = null,
        url: String? = null,
        store: String? = null,
        notes: String? = null,
        platform: String? = null,
        genre: String? = null,
        year: Int? = null,
        rating: String? = null,
        dueDate: String? = null,
        priority: String? = null
    ): Result<DomainShoppingList> {
        
        // Validate input
        if (listId.isBlank()) {
            return Result.failure(IllegalArgumentException("List ID cannot be empty"))
        }
        
        if (itemName.isBlank()) {
            return Result.failure(IllegalArgumentException("Item name cannot be empty"))
        }
        
        val itemRequest = DomainAddItemRequest(
            name = itemName,
            listType = listType,
            price = price,
            quantity = quantity,
            url = url,
            store = store,
            notes = notes,
            platform = platform,
            genre = genre,
            year = year,
            rating = rating,
            dueDate = dueDate,
            priority = priority
        )
        
        return try {
            shoppingListRepository.addItemToList(listId, itemRequest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
