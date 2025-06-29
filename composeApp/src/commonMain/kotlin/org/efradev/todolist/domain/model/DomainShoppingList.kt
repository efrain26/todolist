package org.efradev.todolist.domain.model

/**
 * Domain model for Shopping List
 * 
 * This represents a shopping list in the domain layer, containing all the essential
 * information needed for business logic operations.
 */
data class DomainShoppingList(
    val id: String,
    val name: String,
    val createdAt: String,
    val userId: String,
    val type: String,
    val items: List<DomainShoppingItem> = emptyList()
)
