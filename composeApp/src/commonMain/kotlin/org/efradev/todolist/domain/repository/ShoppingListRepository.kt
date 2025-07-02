package org.efradev.todolist.domain.repository

import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainAddItemRequest

/**
 * Domain repository interface for Shopping List operations
 * 
 * This interface defines shopping list operations in domain terms,
 * using domain models instead of data DTOs.
 * The actual implementation will be in the data layer.
 */
interface ShoppingListRepository {
    suspend fun getShoppingLists(): Result<List<DomainShoppingList>>
    suspend fun createList(name: String, type: String = "simple"): Result<DomainShoppingList>
    suspend fun getShoppingListDetails(listId: String): Result<DomainShoppingList>
    suspend fun addItemToList(listId: String, item: DomainAddItemRequest): Result<DomainShoppingList>
}
